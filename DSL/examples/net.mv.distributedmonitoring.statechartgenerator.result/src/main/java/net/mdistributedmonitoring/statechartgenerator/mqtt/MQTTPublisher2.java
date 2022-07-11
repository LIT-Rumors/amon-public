package net.mdistributedmonitoring.statechartgenerator.mqtt;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import net.mv.logging.ILogger;
import net.mv.logging.LoggerProvider;


public class MQTTPublisher2 implements IMessagePublisher {

	private static final ILogger LOGGER = LoggerProvider.getLogger(MQTTPublisher2.class);


	private String broker;// = "tcp://localhost:1883";
	private MqttClient mqttClient;
	final MemoryPersistence memoryPersistence = new MemoryPersistence();
	
	public MQTTPublisher2() {

	}

	@Override
	public void connect(String broker) throws PublishException {
		this.broker = broker;
		try {
			mqttClient = new MqttClient(broker, MqttClient.generateClientId(),memoryPersistence);

			MqttConnectOptions connOpts = new MqttConnectOptions();
			connOpts.setCleanSession(true); // no persistent session
			connOpts.setKeepAliveInterval(1000);
			connOpts.setMaxInflight(250);
			int qos = 1;

			mqttClient.connect(connOpts);
			mqttClient.setCallback(new MqttCallback() {

				@Override
				public void messageArrived(String topic, MqttMessage message) throws Exception {
					// TwinManager.getInstance().process(topic, new String(message.getPayload()));
				}

				@Override
				public void deliveryComplete(IMqttDeliveryToken token) {
				}

				@Override
				public void connectionLost(Throwable cause) {
					LOGGER.warn("Connection Lost!");
				}
			});
			LOGGER.info("Connected to MQTT server - " + broker +" - " + mqttClient.isConnected());
		} catch (MqttException e) {
			throw new PublishException(e.getMessage());
		}
	}

	@Override
	public void publish(String topic, String message) throws PublishException {
		if (mqttClient == null || !mqttClient.isConnected()) {
			
			throw new RuntimeException("Not connected! Can't connect to "+broker);
		}
		try {
			MqttMessage msg = new MqttMessage();
			msg.setPayload(message.getBytes());

			mqttClient.publish(topic, msg);
		} catch (Exception e) {
			throw new PublishException(topic+"--"+e.getMessage());
		}
	}
	

}
