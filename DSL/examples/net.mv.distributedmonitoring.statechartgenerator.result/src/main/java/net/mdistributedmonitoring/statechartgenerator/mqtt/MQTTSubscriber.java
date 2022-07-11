package net.mdistributedmonitoring.statechartgenerator.mqtt;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import net.mv.logging.ILogger;
import net.mv.logging.LoggerProvider;


/**
 *
 *
 * Utility Class for connecting to an MQTT broker and subscribing to topics
 * 
 * @author michael
 *
 */
public class MQTTSubscriber {

	private static final ILogger LOGGER = LoggerProvider.getLogger(MQTTSubscriber.class);

	private final String broker;// = "tcp://localhost:1883";
	private MqttClient mqttClient;

	private IMessageHander handler;

	public MQTTSubscriber(String broker) {
		this.broker = broker;
	}
	
	final MemoryPersistence memoryPersistence = new MemoryPersistence();

	public void connect(IMessageHander handler) throws MqttSecurityException, MqttException {
		this.handler =handler;
		mqttClient = new MqttClient(broker, MqttClient.generateClientId(),memoryPersistence);
		MqttConnectOptions connOpts = new MqttConnectOptions();
		connOpts.setCleanSession(true); // no persistent session
		connOpts.setKeepAliveInterval(1000);
		int qos = 1;

		mqttClient.connect();
		mqttClient.setCallback(new MqttCallback() {

			@Override
			public void messageArrived(String topic, MqttMessage message) throws Exception {
				LOGGER.info("message received: "+topic+" -- "+ new String(message.getPayload()));
//				TimingObject t = TimingObject.create();
//				TwinManager.getInstance().process(topic, new String(message.getPayload()),t);
				handler.messageArrive(topic,new String(message.getPayload()));
			}

			@Override
			public void deliveryComplete(IMqttDeliveryToken token) {
			}

			@Override
			public void connectionLost(Throwable cause) {
				LOGGER.error(cause);
			}
		});
		LOGGER.info("Connected to MQTT server" + broker);
	}

	public void subscribe(String topic) throws MqttException {
		if (mqttClient == null || !mqttClient.isConnected()) {
			throw new RuntimeException("Not connected!");
		}
		LOGGER.info("Subscribing to '"+topic+"'");
		mqttClient.subscribe(topic);
	}

}
