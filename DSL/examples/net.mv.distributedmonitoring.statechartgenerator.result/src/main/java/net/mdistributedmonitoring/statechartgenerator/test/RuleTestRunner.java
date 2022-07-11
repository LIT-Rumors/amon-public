package net.mdistributedmonitoring.statechartgenerator.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.mdistributedmonitoring.statechartgenerator.adative.MonitoringAdaptationManager;
import net.mdistributedmonitoring.statechartgenerator.adative.resolve.RuleResolver;
import net.mdistributedmonitoring.statechartgenerator.mqtt.MQTTPublisher;

public class RuleTestRunner {

	static final String[] ALL_EVENTS = { "ev_1", "ev_2", "ev_3", "ev_4", "ev_5", "ev_6", "ev_7", "ev_8", "ev_9",
			"ev_10", "ev_11", "ev_12", "ev_13", "ev_14", "ev_15", "ev_16", "ev_17", "ev_18", "ev_19", "ev_20" };
	static Random r = new Random();
	static MQTTPublisher pub = MQTTPublisher.getInstance();

	public static void main(String[] args) {
		try {
			// RuleResolver.executeErrorTrigger(null, null);

			MonitoringAdaptationManager.getInstance().init("test");

			Thread.sleep(5000);

			pub.connect("tcp://localhost:1883");
			Thread.sleep(60000);
			
			for (int i = 0; i < 500; i++) {
				System.out.println("ITER:" + i);
				int event_count = r.nextInt(3) + 1;
				sendRandom(event_count);
				Thread.sleep(500);
			}

			// pub.publish("monitoring/context", "{ 'name':'battery','type':'*UB*',
			// 'status':'active'}");

			// Thread.sleep(33000);

			TimeLogger.report();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void sendRandom(int numevents) throws Exception {

		List<String> activated = new ArrayList<>();

		System.out.println("SENDING EVENTS: " + numevents);
		for (int i = 0; i < numevents; i++) {
			int eventnum = r.nextInt(20); // + 1;
			String eventName = ALL_EVENTS[eventnum];
			System.out.println(eventName);
			activated.add(eventName);
			if (i == numevents - 1) {
				String msg = String.format("{ 'name':'%s','type':'*UB*', 'status':'active'}", eventName);
				pub.publish("monitoring/context", msg);
				Thread.sleep(350);
			} else {
				RuleResolver.addTrigger(eventName);
			}

		}
		for (String ac : activated) {
			RuleResolver.removeErrorTrigger(ac);
		}
	}

}
