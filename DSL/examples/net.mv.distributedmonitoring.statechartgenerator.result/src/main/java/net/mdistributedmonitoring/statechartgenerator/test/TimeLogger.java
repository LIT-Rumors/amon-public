package net.mdistributedmonitoring.statechartgenerator.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TimeLogger {

	final static List<Long> measurements = new ArrayList<>();
	final static List<String> activations = new ArrayList<>();

	public static void report() {
		for (Long meas : measurements) {
			System.out.println(meas);
		}
		for (String act : activations) {
			System.out.println(act);
		}
	}

	public static void measure(long end) {
		measurements.add(end);

	}

	public static void logActivation(String name, Set<String> activeTrigger) {
		String activeList = activeTrigger.stream().collect(Collectors.joining(","));
		activations.add(name + ',' + activeList);

	}

}
