package net.mdistributedmonitoring.statechartgenerator.adative;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import net.mdistributedmonitoring.statechartgenerator.adative.MessageForwarder.LockObject;
import net.mdistributedmonitoring.statechartgenerator.adative.MonitoringAdaptationManager.Scope;
import net.mv.logging.ILogger;
import net.mv.logging.LoggerProvider;

public class MessageForwarder {

	public class LockObject {

	}

	private static final ILogger LOGGER = LoggerProvider.getLogger(MessageForwarder.class);

	private ScheduledExecutorService reportLocal = Executors.newSingleThreadScheduledExecutor();
	private ScheduledExecutorService reportGlobal = Executors.newSingleThreadScheduledExecutor();
	IMonitorableMessage message;
//	private long frequency;
	private long changeFrequency;
	private long reportFrequency;

	private long currentTS = 0;
	private long previousTS = 0;

	private String monitor;

	private Scope scope = Scope.LOCAL;
	private LockObject lockObject = new LockObject();
	private AtomicBoolean sent = new AtomicBoolean(false);

	public MessageForwarder(String monitor, long changeFrequency, long reportFrequency) {
		this.monitor = monitor;
		this.changeFrequency = changeFrequency;
		this.reportFrequency = reportFrequency;

	}

	public void postMessage(IMonitorableMessage message) {
		synchronized (lockObject) {
			this.message = message;
			previousTS = currentTS;
			currentTS = System.currentTimeMillis();

			sent.set(false);
		}
	}

	public void init() {

		if (changeFrequency > 0) {
			LOGGER.info("Initializing forwarder for " + monitor + " with " + changeFrequency);
			reportLocal.scheduleAtFixedRate(this::executePeriodically, 0, changeFrequency, TimeUnit.SECONDS);
		} else {
			LOGGER.info("Shutting-Down forwarder for " + monitor);
		}
//		if (reportFrequency > 0) {
//			reportGlobal.scheduleAtFixedRate(this::reportPeriodically, 0, reportFrequency, TimeUnit.SECONDS);
//		}
	}

	public void updateFrequency(long changeFrequency) {
		this.changeFrequency = changeFrequency;
		try {
			shutDown();
			init();
		} catch (Throwable e) {
			LOGGER.error(e);
		}
	}

	private void executePeriodically() {
		try {
			publishMessage();
		} catch (Throwable e) {
			LOGGER.error(e);
		}

	}

	private void reportPeriodically() {
		System.out.println("Reprt " + monitor);

	}

	private void shutDown() {
		reportLocal.shutdown();
		reportGlobal.shutdown();
		reportLocal = Executors.newSingleThreadScheduledExecutor();
		reportGlobal = Executors.newSingleThreadScheduledExecutor();
	}

	public void changeScope(Scope scope) {
		this.scope = scope;

	}

	private void publishMessage() {
		synchronized (lockObject) {
			if (message != null) {
				//LOGGER.info("Publishing -  " + monitor + "to: " + scope);
				MQTTDistributor.getInstance().publishMessage(message, scope);
				if (sent.get()) {
					//LOGGER.info(monitor + " >> Message has anready been sent!");
					MQTTDistributor.getInstance().publishMonitorInfo("Freq. Error for " + monitor + " ("
							+ (currentTS - previousTS) + " / timer: " + changeFrequency * 1000 + ")");
				}
				sent.set(true);
			} else {
				//LOGGER.info("Can not publish -  " + monitor + " no message received!");
			}
		}
	}

}
