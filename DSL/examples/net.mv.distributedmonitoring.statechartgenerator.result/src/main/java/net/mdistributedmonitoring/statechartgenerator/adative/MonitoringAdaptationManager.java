package net.mdistributedmonitoring.statechartgenerator.adative;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.mdistributedmonitoring.statechartgenerator.statemachine.EventTrigger;
import net.mdistributedmonitoring.statechartgenerator.statemachine.StateMachineManager;
import net.mdistributedmonitoring.statechartgenerator.statemachine.TriggerFactory;
import net.mv.logging.ILogger;
import net.mv.logging.LoggerProvider;

public class MonitoringAdaptationManager {

	private static volatile MonitoringAdaptationManager INSTANCE;
	private static final ILogger LOGGER = LoggerProvider.getLogger(MonitoringAdaptationManager.class);
	Map<String, MessageForwarder> timingThreads = new HashMap<>();
	private StateMachineManager m;
	private String id = UUID.randomUUID().toString();

	public enum Scope {
		LOCAL, CENTRAL
	}

	public static MonitoringAdaptationManager getInstance() {
		if (INSTANCE == null) {
			synchronized (MonitoringAdaptationManager.class) {
				if (INSTANCE == null) {
					INSTANCE = new MonitoringAdaptationManager();
				}
			}
		}
		return INSTANCE;
	}

	public void changeFrequency(String property, long frequency) {
		if (timingThreads.containsKey(property)) {
			timingThreads.get(property).updateFrequency(frequency);
		} else {
			MessageForwarder ts = new MessageForwarder(property, frequency, frequency);
			ts.init();
			timingThreads.put(property, ts);
		}
	}

	public void changeScope(String property, Scope scope) {
		if (timingThreads.containsKey(property)) {
			timingThreads.get(property).changeScope(scope);
		} else {
			LOGGER.error("Can not change Scope - No frequnecy defined for property " + property);
		}
	}

	public void postMessageOnce(IMonitorableMessage message) {
		try {

			MQTTDistributor.getInstance().publishMessage(message, Scope.CENTRAL);

		} catch (Exception e) {
			LOGGER.error(e);
		}

	}

	public void postMessage(IMonitorableMessage message) {
		try {
//			if(true) {
//				

//				return;
//			}

			MessageForwarder forwarder = timingThreads.get(message.getType());
			if (forwarder != null) {
				forwarder.postMessage(message);
			} else {
//				LOGGER.error("No rules defined for " + message.getType());
//				MQTTDistributor.getInstance().publishMonitorInfo(
//						id + ",No rule active for " + message.getType() + " in context [" + m.getContext() + "]");
			}
		} catch (Exception e) {
			LOGGER.error(e);
		}
	}

	public void updateContext(String trigger) {
		try {
			LOGGER.info("Update Context " + trigger);
			EventTrigger t = new EventTrigger(trigger);

			MQTTDistributor.getInstance().publishContext(id,TriggerFactory.TriggerToString(t));
		} catch (Exception e) {
			LOGGER.error(e);
		}
	}

	public void notifyRuleChange(String ruleName) {
		MQTTDistributor.getInstance().publishMonitorInfo(id + ",ruleactive", "Rule " + ruleName + " activated");
	}

	public void init(String id) {
		try {
			this.id = id;
			MQTTDistributor.getInstance().initPublishers();
			m = new StateMachineManager();
			
			m.init();
			m.connect();
		} catch (Exception e) {
			LOGGER.error(e);
		}

	}

	public String getId() {
		return id;
	}

	public void notifyStateChange(String type, String state) {
		MQTTDistributor.getInstance().publishMonitorInfo(id + ",statechange," + type + "," + state);

	}

	public void notifyStateChange(String oldstate, String newstate, String trigger) {
		String message = MSQTransformer.toJSONString("oldstate",oldstate,"newstate",newstate,"trigger",trigger);
		MQTTDistributor.getInstance().publishMonitorInfo("statechange/"+id,message);		
	}

}
