package net.mdistributedmonitoring.statechartgenerator.statemachine;

import java.util.HashMap;
import java.util.Map;

import net.mdistributedmonitoring.statechartgenerator.generated.GeneratedStateMachine.State;
import net.mdistributedmonitoring.statechartgenerator.generated.GeneratedStateMachine.Trigger;




public class TriggerConfig {
	Map<State, Trigger> stateTriggerMap = new HashMap<>();

	public Trigger getTrigger(State state) {
		return stateTriggerMap.get(state);
	}
	
	public Trigger addTrigger(State state, Trigger trigger) {
		return stateTriggerMap.put(state, trigger);
	}
}
