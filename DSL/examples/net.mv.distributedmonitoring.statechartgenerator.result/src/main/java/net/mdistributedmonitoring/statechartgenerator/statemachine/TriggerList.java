package net.mdistributedmonitoring.statechartgenerator.statemachine;

import java.util.HashMap;
import java.util.Map;

import net.mdistributedmonitoring.statechartgenerator.generated.GeneratedStateMachine.State;
import net.mdistributedmonitoring.statechartgenerator.generated.GeneratedStateMachine.Trigger;

public class TriggerList {

	private Map<String, TriggerConfig> triggerList = new HashMap<>();

	public void add(String eventname, State state, Trigger trigger) {
		if (triggerList.containsKey(eventname)) {
			TriggerConfig config = triggerList.get(eventname);
			config.addTrigger(state, trigger);
		} else {

			TriggerConfig c = new TriggerConfig();
			c.addTrigger(state, trigger);
			triggerList.put(eventname, c);
		}

	}

	public Trigger getTrigger(String name, State state) {
		if (triggerList.containsKey(name)) {
			TriggerConfig tc = triggerList.get(name);
			return tc.getTrigger(state);
		}
		return null;
	}

}
