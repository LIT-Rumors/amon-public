package net.mdistributedmonitoring.statechartgenerator.mqtt;

public interface IMessageHander {

	void messageArrive(String topic, String message);

}
