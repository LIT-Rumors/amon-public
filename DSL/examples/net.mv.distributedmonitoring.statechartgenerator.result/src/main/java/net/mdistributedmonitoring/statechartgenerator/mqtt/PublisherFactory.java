package net.mdistributedmonitoring.statechartgenerator.mqtt;

public class PublisherFactory {
	
	

	private static volatile PublisherFactory instance;

	public static PublisherFactory getInstance() {
		if (instance == null) {
			synchronized (PublisherFactory.class) {
				if (instance == null) {
					instance = new PublisherFactory();
				}
			}
		}
		return instance;
	}
	
	public IMessagePublisher getPublisher() {
		return MQTTPublisher.getInstance();
	}

}
