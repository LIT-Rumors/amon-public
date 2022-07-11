#!/usr/bin/env python
import os
import sys

from paho.mqtt import client as mqtt_client

dir_path = os.path.dirname(os.path.realpath(__file__))
sys.path.append(dir_path)

from mqtt_forwarder import LOCAL_MQTT_IP
from data_collector import TRIGGER_TOPIC_STRING

LOCAL_STATE_MACHINE_MQTT_ID = 'mqtt-state-machine-mqtt'
global NOT_SET_STRING, CURRENT_STATE, INVALID_STATE_STRING, local_sm_sub_client


TRANSITION_OBJECTS = [    
     {'source': 'initialized',                  'transition': 'activate',               'target':  'active'},
     {'source': 'active',                       'transition': 'start_calibration',      'target':  'calibrating'},
 {'source': 'calibrating',                      'transition': 'calibration_finished',   'target':  'ready'},

     {'source': 'ready',                        'transition': 'mission_started',        'target':  'in_home_zone'},
     {'source': 'in_home_zone',                 'transition': 'leaving_home',           'target':  'travelling_to_target_location'},
     {'source': 'travelling_to_target_location','transition': 'entering_target_area',   'target':  'enter_target_zone'},
 {'source': 'enter_target_zone',                'transition': 'target_reached',         'target':  'target_location_reached'},
     {'source': 'target_location_reached',      'transition': 'ready_for_pickup',       'target':  'waiting_for_pickup'},
     {'source': 'waiting_for_pickup',           'transition': 'pickup_complete',        'target':  'return_to_home_location'},
 {'source': 'return_to_home_location',          'transition': 'entering_home',          'target':  'in_home_zone'},
     {'source': 'in_home_zone',                 'transition': 'mission_finished',       'target':  'ready'}]
        

def __is_trigger_valid(trigger):
    global INVALID_STATE_STRING, CURRENT_STATE
    # check if trigger results in valid state change
    for transition in TRANSITION_OBJECTS:
        # find the right transition and check if it is valid
        if transition['transition'] == trigger and transition['source'] == CURRENT_STATE:
            # trigger is valid, return next state
            return transition['target']

    # found no valid trigger, stay in the same state
    return INVALID_STATE_STRING
    

def __on_connect(client, userdata, flags, rc):
    if rc == 0:
        print("Connected to MQTT Broker!")
    else:
        print("Failed to connect, return code %d \n ", rc)


def __publish_new_state(new_state):
    global local_sm_sub_client
    print(f'found new valid state: {new_state}')
    local_sm_sub_client.publish('statemachine/new_state', new_state)


def __handle_new_trigger(trigger):
    global CURRENT_STATE, NOT_SET_STRING, INVALID_STATE_STRING

    # check if this is the initial first state
    if CURRENT_STATE == NOT_SET_STRING and TRANSITION_OBJECTS[0]['transition'] == trigger:
        new_state = TRANSITION_OBJECTS[0]['target']
    else:
        new_state = __is_trigger_valid(trigger)

    if new_state != INVALID_STATE_STRING:
        CURRENT_STATE = new_state
        __publish_new_state(new_state)
    else:
        print(f'Statemachine got invalid trigger: {trigger}')


def __on_trigger_message(client, userdata, message):
    trigger = str(message.payload.decode("utf-8"))
    print("new trigger received: ", trigger,
          "; topic ", message.topic, "; retained ", message.retain)
    __handle_new_trigger(trigger)
    if message.retain == 1:
        print("This is a retained message")


def __init_constants():
    global NOT_SET_STRING, CURRENT_STATE, INVALID_STATE_STRING
    NOT_SET_STRING = 'NOT_SET'
    CURRENT_STATE = NOT_SET_STRING
    INVALID_STATE_STRING = 'INVALID_STATE'


def __init_mqtt():
    global local_sm_sub_client
    # create mqtt client
    local_sm_sub_client = mqtt_client.Client(LOCAL_STATE_MACHINE_MQTT_ID)
    local_sm_sub_client.on_connect = __on_connect
    local_sm_sub_client.connect(LOCAL_MQTT_IP, 1883)
    # reset the statemachine topics from previous runs
    local_sm_sub_client.publish('statemachine/new_state', '')
    local_sm_sub_client.publish('SuM/trigger', '')
    # subscribe to triggers
    local_sm_sub_client.subscribe(TRIGGER_TOPIC_STRING)
    local_sm_sub_client.on_message = __on_trigger_message
    print('initialized statemachine MQTT')
    local_sm_sub_client.loop_forever()


def __main():
    # init vars
    __init_constants()

    # init MQTT
    __init_mqtt()


if __name__ == '__main__':
    print('Starting statemachine')
    __main()

