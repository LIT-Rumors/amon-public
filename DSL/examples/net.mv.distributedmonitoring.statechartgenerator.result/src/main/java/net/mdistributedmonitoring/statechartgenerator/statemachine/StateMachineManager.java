package net.mdistributedmonitoring.statechartgenerator.statemachine;

import org.eclipse.paho.client.mqttv3.MqttException;

import com.github.oxo42.stateless4j.StateMachine;

import net.mdistributedmonitoring.statechartgenerator.adative.MQTTDistributor;
import net.mdistributedmonitoring.statechartgenerator.adative.MonitoringAdaptationManager;
import net.mdistributedmonitoring.statechartgenerator.generated.GeneratedStateMachine;
import net.mdistributedmonitoring.statechartgenerator.generated.GeneratedStateMachine.State;
import net.mdistributedmonitoring.statechartgenerator.generated.GeneratedStateMachine.Trigger;
import net.mdistributedmonitoring.statechartgenerator.mqtt.MQTTSubscriber;
import net.mdistributedmonitoring.statechartgenerator.mqtt.PublishException;
import net.mv.logging.ILogger;
import net.mv.logging.LoggerProvider;

public class StateMachineManager {
	private static final ILogger LOGGER = LoggerProvider.getLogger(StateMachineManager.class);

	StateMachine<State, Trigger> sm;
	GeneratedStateMachine s = new GeneratedStateMachine();
	private MQTTSubscriber subscriber;

	public void init() {
		s.init();
		sm = new StateMachine<>(State.initialized, s.getStateMachineConfig());
	}

	public void doTransition(EventTrigger e) {
		Trigger trigger = getTrigger(e);
		if (trigger != null) {
			State oldstate = sm.getState();

			sm.fire(trigger);
			State newState = sm.getState();
			LOGGER.info("Firing trigger '" + trigger.name() + "' from '" + oldstate.name() + "' to '" + newState.name()
					+ "'");
			MQTTDistributor.getInstance().publishMonitorInfo("stateactive", newState.name());
			MonitoringAdaptationManager.getInstance().notifyStateChange(oldstate.name(),newState.name(),trigger.name());
		} else {
			LOGGER.warn("No trigger for " + e.getName() + " in state " + sm.getState().name());
		}
	}

	private Trigger getTrigger(EventTrigger e) {
		TriggerList triggerList = s.getTriggerList();
		return triggerList.getTrigger("Trigger["+e.getName()+"]", sm.getState());
	}

	public void connect() throws PublishException {
		subscriber = new MQTTSubscriber("tcp://localhost:1883");
		try {
			subscriber.connect((topic, message) -> handleMessage(topic, message));
			subscriber.subscribe("monitoring/context");
		} catch (MqttException e) {
			LOGGER.error(e);
			throw new PublishException(e.getMessage());

		}

	}

	private void handleMessage(String topic, String message) {
		try {
			EventTrigger trigger = TriggerFactory.getTrigger(message);

			if (trigger instanceof ErrorTrigger) {
				if (((ErrorTrigger) trigger).activate()) {
					setErrorState(trigger);
				} else {
					leavErrorState(trigger);
				}
			} else {
				doTransition(new EventTrigger(trigger.getName()));
			}
		} catch (Exception e) {
			LOGGER.error( message, e);
		}

	}

	private void setErrorState(EventTrigger trigger) {
		LOGGER.info("Error State" + trigger.toString());
		s.executeErrorState(trigger.getName());

	}

	private void leavErrorState(EventTrigger trigger) {
		LOGGER.info("Error State deactivated" + trigger.toString());
		s.leavErrorState(trigger.getName(), sm.getState().name());

	}

	public String getContext() {
		return sm.getState().name();
	}

}
