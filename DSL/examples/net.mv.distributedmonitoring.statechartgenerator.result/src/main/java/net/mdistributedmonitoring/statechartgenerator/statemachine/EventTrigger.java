package net.mdistributedmonitoring.statechartgenerator.statemachine;

import java.io.Serializable;

public class EventTrigger implements Serializable {
	private String name;

	public EventTrigger(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	
}