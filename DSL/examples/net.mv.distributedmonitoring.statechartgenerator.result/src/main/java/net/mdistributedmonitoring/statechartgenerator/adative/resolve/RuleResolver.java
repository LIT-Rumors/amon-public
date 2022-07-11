package net.mdistributedmonitoring.statechartgenerator.adative.resolve;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.mdistributedmonitoring.statechartgenerator.adative.IURule;
import net.mdistributedmonitoring.statechartgenerator.adative.MQTTDistributor;
import net.mdistributedmonitoring.statechartgenerator.adative.MessageForwarder;
import net.mdistributedmonitoring.statechartgenerator.adative.MonitoringAdaptationManager;
import net.mdistributedmonitoring.statechartgenerator.test.TimeLogger;
import net.mv.logging.ILogger;
import net.mv.logging.LoggerProvider;

public class RuleResolver {

	private static final int WEIGHT_1 = 1;
	private static final int WEIGHT_2 = 1;
	private static Set<String> activeTrigger = new HashSet<>();

	private static final ILogger LOGGER = LoggerProvider.getLogger(RuleResolver.class);

	public static void executeErrorTrigger(String trigger, ArrayList<IURule> rules) {
		activeTrigger.add(trigger);
		List<RuleScore> result = checkRules(rules);

		Collections.sort(result, new ScoreComparator());
		Collections.reverse(result);
		if (rules.size() > 0 && result.get(0).getScore() > 0) {
			IURule rule = result.get(0).getRule();

			rule.executeRule();
			LOGGER.info("Executing new Rule: " + rule.getName());
			TimeLogger.logActivation(rule.getName(), activeTrigger);
			String message = MonitoringAdaptationManager.getInstance().getId() + ",Error Trigger -" + trigger + "-"
					+ rule.getName() + "-" + result.get(0).getScore() + "-S:" + rule.getSalience();
			MQTTDistributor.getInstance().publishMonitorInfo(message);
			LOGGER.info(message);
		} else {
			LOGGER.info("No Rule to execute");
		}

	}

	public static void addTrigger(String trigger) {
		activeTrigger.add(trigger);
	}

	public static void checkForNewTriggers(ArrayList<IURule> rules) {
		List<RuleScore> result = checkRules(rules);

		Collections.sort(result, new ScoreComparator());
		Collections.reverse(result);
		if (rules.size() > 0 && result.get(0).getScore() > 0) {
			IURule rule = result.get(0).getRule();

			rule.executeRule();
			LOGGER.info("Executing new Rule: " + rule.getName());
			String message = MonitoringAdaptationManager.getInstance().getId() + ",Error Trigger -"
					+ activeTrigger.toString() + "-" + rule.getName() + "-" + result.get(0).getScore() + "-S:"
					+ rule.getSalience();
			MQTTDistributor.getInstance().publishMonitorInfo(message);
			LOGGER.info(message);
		} else {
			LOGGER.info("No Rule to execute");
		}

	}

	public static void removeErrorTrigger(String trigger) {
		activeTrigger.remove(trigger);
		LOGGER.info("Deactivating Error State" + trigger + "Error States active:" + activeTrigger.size());
	}

	public static boolean errorStateActive() {
		return activeTrigger.size() > 0;
	}

	private static List<RuleScore> checkRules(ArrayList<IURule> rules) {
		List<RuleScore> scores = new ArrayList<>();
		for (IURule rule : rules) {
			RuleScore s = caluclateScore(rule);
			scores.add(s);
		}
		return scores;

	}

	private static RuleScore caluclateScore(IURule rule) {

		List<String> active = new ArrayList<>(activeTrigger);
		List<String> ruleTrigger = new ArrayList<>(rule.getConditions());

		Set<String> comlA = getComplement(active, ruleTrigger);
		Set<String> comlB = getComplement(ruleTrigger, active);
		Set<String> union = new HashSet<>(active);
		union.addAll(ruleTrigger);

		double penalty = (comlA.size() * WEIGHT_1 + comlB.size() * WEIGHT_2) / ((double) union.size());
		double score = 1 - penalty;

//		ArrayList col = new ArrayList(activeTrigger);
//		col.retainAll(rule.getConditions());
//		double score = (col.size() / (double) rule.getConditions().size());
		// LOGGER.info("Score for Rule:" + rule.getName() + "->" + score);
		return new RuleScore(score, rule);

	}

	private static Set<String> getComplement(List<String> a, List<String> b) {
		Set<String> differenceSet = new HashSet<>(a);
		differenceSet.removeAll(b);
		return differenceSet;
	}

}
