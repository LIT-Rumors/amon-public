package net.mdistributedmonitoring.statechartgenerator.statemachine;

public class ErrorTrigger extends EventTrigger {

	public ErrorTrigger(String name) {
		super(name);

	}

	String status;
	
	
	@Override
	public String toString() {
		return "["+super.getName()+"] --> " + status;
	}
	
	public boolean activate() {
		return "active".equals(status); 
	}

	
}
