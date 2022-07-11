package net.mdistributedmonitoring.statechartgenerator.adative;

public interface IMonitorableMessage {

	String getTopic();

	String getMessage();

	String getType();

}
