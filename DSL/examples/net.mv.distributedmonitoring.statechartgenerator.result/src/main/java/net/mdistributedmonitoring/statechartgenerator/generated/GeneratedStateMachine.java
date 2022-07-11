package net.mdistributedmonitoring.statechartgenerator.generated;

import net.mdistributedmonitoring.statechartgenerator.statemachine.TriggerList;
import com.github.oxo42.stateless4j.StateMachineConfig;
import net.mdistributedmonitoring.statechartgenerator.adative.generated.AdaptationExecutor;

public class GeneratedStateMachine {

	private final StateMachineConfig<State, Trigger> smConfig = new StateMachineConfig<>();
	private final TriggerList triggerList = new TriggerList();
	private final AdaptationExecutor exec = new AdaptationExecutor();

	public void init() {
		configureStateMachine();
		configureTransitions();
	}

	public TriggerList getTriggerList() {
		return triggerList;
	}

	public StateMachineConfig<State, Trigger> getStateMachineConfig() {
		return smConfig;
	}

	public void onEntry(State state) {
		System.out.println("Entry:" + state.name());
		exec.executeTrigger(state.name());
	}

	public void onExit(State state) {
		System.out.println("Exit:" + state.name());
	}

	public void executeErrorState(String statename) {
		System.out.println("Entry Error State:" + statename);
		exec.executeErrorTrigger(statename);
	}

	public void leavErrorState(String name, String currentState) {
		System.out.println("Exit Error State:" + name);
		exec.leaveErrorState(name, currentState);
	}

	public void configureTransitions() {
		smConfig.configure(State.initialized).permit(Trigger.active, State.active);
		triggerList.add("Trigger[active]", State.initialized, Trigger.active);
		smConfig.configure(State.active).permit(Trigger.ready, State.ready_for_drive);
		triggerList.add("Trigger[ready]", State.active, Trigger.ready);
		smConfig.configure(State.ready_for_drive).permit(Trigger.driving, State.drive);
		triggerList.add("Trigger[driving]", State.ready_for_drive, Trigger.driving);
		smConfig.configure(State.drive).permit(Trigger.stopping, State.ready_for_drive);
		triggerList.add("Trigger[stopping]", State.drive, Trigger.stopping);
	}

	public void configureStateMachine() {
		smConfig.configure(State.active).onEntry(() -> onEntry(State.active)).onExit(() -> onExit(State.active));
		smConfig.configure(State.initialized).onEntry(() -> onEntry(State.initialized))
				.onExit(() -> onExit(State.initialized));
		smConfig.configure(State.ready_for_drive).onEntry(() -> onEntry(State.ready_for_drive))
				.onExit(() -> onExit(State.ready_for_drive));
		smConfig.configure(State.drive).onEntry(() -> onEntry(State.drive)).onExit(() -> onExit(State.drive));
	}
 public static enum State{ active,
 initialized,
 ready_for_drive,
 drive,
}
 public static enum Trigger{ active,
 ready,
 driving,
 stopping,
}}