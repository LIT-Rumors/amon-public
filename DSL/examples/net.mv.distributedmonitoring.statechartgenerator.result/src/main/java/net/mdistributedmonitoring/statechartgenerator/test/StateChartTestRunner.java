package net.mdistributedmonitoring.statechartgenerator.test;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import net.mdistributedmonitoring.statechartgenerator.adative.MonitoringAdaptationManager;
import net.mdistributedmonitoring.statechartgenerator.mqtt.MQTTPublisher;
import net.mdistributedmonitoring.statechartgenerator.mqtt.PublishException;
import net.mdistributedmonitoring.statechartgenerator.statemachine.StateMachineManager;

public class StateChartTestRunner {

	private static boolean sendMessages = false;

	public static void main(String[] args) {

	StateMachineManager m = new StateMachineManager();

		try {
//			m.init();
//			m.connect();
			if (sendMessages) {
				sendMessages();
			}
			MonitoringAdaptationManager.getInstance().init("test");

			Thread.sleep(5000);

			MQTTPublisher pub = MQTTPublisher.getInstance();
			pub.connect("tcp://localhost:1883");
			
			Thread.sleep(5000);
			System.out.println("SEND MESSAGE");
			pub.publish("monitoring/context", "{ 'name':'Trigger[ready]'}");
			Thread.sleep(15000);
			System.out.println("SEND MESSAGE");
			pub.publish("monitoring/context", "{ 'name':'battery','type':'*UB*', 'status':'active'}");
			Thread.sleep(15000);
			System.out.println("SEND MESSAGE");
			pub.publish("monitoring/context", "{ 'name':'Trigger[sync_takeoff]'}");
			Thread.sleep(30000);
			System.out.println("SEND MESSAGE");
			//pub.publish("monitoring/context", "{ 'name':'Trigger[sync_takeoff]'}");
			pub.publish("monitoring/context", "{ 'name':'battery','type':'*UB*', 'status':'inactive'}");

			Thread.sleep(33000);

//			pub.publish("monitoring/context", "{ 'name':'Trigger[sync_takeoff]'}");
//			Thread.sleep(5000);
//			pub.publish("monitoring/context", "{ 'name':'Trigger[sync-to]'}");
//
//			Thread.sleep(5000);
//			pub.publish("monitoring/context", "{ 'name':'Trigger[syncx-to]'}");
//			
//			Thread.sleep(5000);
//			pub.publish("monitoring/context", "{ 'name':'battery','type':'*UB*', 'status':'active'}");

		} catch (PublishException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static final ScheduledExecutorService exec = Executors.newScheduledThreadPool(20);

	private static void sendMessages() {
		exec.scheduleAtFixedRate(StateChartTestRunner::executePeriodically1, 5, 15, TimeUnit.SECONDS);
		exec.scheduleAtFixedRate(StateChartTestRunner::executePeriodically2, 5, 1, TimeUnit.SECONDS);
		exec.scheduleAtFixedRate(StateChartTestRunner::executePeriodically3, 5, 3, TimeUnit.SECONDS);
	}

	private static void executePeriodically1() {
		MonitorableMessage m = new MonitorableMessage("Drone.State", "uav1/state", "asd");
		MonitoringAdaptationManager.getInstance().postMessage(m);
	}

	private static void executePeriodically2() {
		MonitorableMessage m = new MonitorableMessage("parameters", "uav1/parameters", "xxx");
		MonitoringAdaptationManager.getInstance().postMessage(m);
	}

	private static void executePeriodically3() {
		MonitorableMessage m = new MonitorableMessage("GPS", "uav1/gps", "asd");
		MonitoringAdaptationManager.getInstance().postMessage(m);
	}

}
