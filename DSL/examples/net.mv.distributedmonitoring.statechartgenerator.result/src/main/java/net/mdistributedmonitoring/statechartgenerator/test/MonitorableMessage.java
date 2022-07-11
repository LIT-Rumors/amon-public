package net.mdistributedmonitoring.statechartgenerator.test;

import net.mdistributedmonitoring.statechartgenerator.adative.IMonitorableMessage;

public class MonitorableMessage implements IMonitorableMessage {

	private final String type;
	private final String topic;
	private final String message;

	public MonitorableMessage(String type, String topic, String message) {
		this.type = type;
		this.topic = topic;
		this.message = message;
	}

	public String getType() {
		return type;
	}

	public String getTopic() {
		return topic;
	}

	public String getMessage() {
		return message;
	}

}

