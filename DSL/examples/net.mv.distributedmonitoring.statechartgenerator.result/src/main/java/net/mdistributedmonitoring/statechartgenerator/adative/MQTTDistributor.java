package net.mdistributedmonitoring.statechartgenerator.adative;

import net.mdistributedmonitoring.statechartgenerator.adative.MonitoringAdaptationManager.Scope;
import net.mdistributedmonitoring.statechartgenerator.mqtt.MQTTPublisher2;
import net.mdistributedmonitoring.statechartgenerator.mqtt.PublishException;

public class MQTTDistributor {

	private static final String LOCAL = "tcp://localhost:1883";
	private static final String CENTRAL = "tcp://192.168.0.222:1883";
	private MQTTPublisher2 local;
	private MQTTPublisher2 central;
	private String TOPIC_MONITORING = "monitoring/info";
	private String TOPIC_CONTEXT = "monitoring/context";

	private static volatile MQTTDistributor instance;

	private MQTTDistributor() {

	}

	public static MQTTDistributor getInstance() {
		if (instance == null) {
			synchronized (MQTTDistributor.class) {
				if (instance == null) {
					instance = new MQTTDistributor();
				}
			}
		}
		return instance;
	}

	public void initPublishers() {

		try {
			local = new MQTTPublisher2();
			central = new MQTTPublisher2();

			local.connect(LOCAL);
			central.connect(CENTRAL);

		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void publishMessage(IMonitorableMessage message, Scope scope) {
		if (scope == Scope.LOCAL) {
			publishLocal(message);
		} else {
			publishCentral(message);
		}

	}

	private void publishCentral(IMonitorableMessage message) {
		try {
			central.publish(message.getTopic()+"/"+message.getType(), message.getMessage());
		} catch (PublishException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void publishLocal(IMonitorableMessage message) {
		try {
			local.publish(message.getTopic(), message.getMessage());
		} catch (PublishException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void publishMonitorInfo(String message) {
		try {
			central.publish(TOPIC_MONITORING, message);
		} catch (PublishException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void publishMonitorInfo(String topic, String message) {
		try {
			central.publish(TOPIC_MONITORING + "/" + topic, message);
		} catch (Exception e) {
			// TODO Auto-generated catch block
		//	e.printStackTrace();
			System.out.println("CONNECTION_ERROR");
		}

	}

	public void publishContext(String id, String trigger) {
		try {
			local.publish(TOPIC_CONTEXT, trigger);
			central.publish(TOPIC_CONTEXT+"/"+id, trigger);

		} catch (PublishException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
