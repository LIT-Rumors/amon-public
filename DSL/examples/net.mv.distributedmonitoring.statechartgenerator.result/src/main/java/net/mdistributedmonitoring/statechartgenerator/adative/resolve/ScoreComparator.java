package net.mdistributedmonitoring.statechartgenerator.adative.resolve;

import java.util.Comparator;

public class ScoreComparator implements Comparator<RuleScore> {

	@Override
	public int compare(RuleScore o1, RuleScore o2) {
		int res = Double.compare(o1.getScore(), o2.getScore());
		if (res == 0) { 
			return Integer.compare(o1.getRule().getSalience(), o2.getRule().getSalience());
		}
		return res;

	}

}
