package net.mdistributedmonitoring.statechartgenerator.adative.resolve;

import net.mdistributedmonitoring.statechartgenerator.adative.IURule;

public class RuleScore {

	private final double score;
	private final IURule rule;

	public RuleScore(double score, IURule rule) {
		this.score = score;
		this.rule = rule;
	}

	public double getScore() {
		return score;
	}

	public IURule getRule() {
		return rule;
	}



}
