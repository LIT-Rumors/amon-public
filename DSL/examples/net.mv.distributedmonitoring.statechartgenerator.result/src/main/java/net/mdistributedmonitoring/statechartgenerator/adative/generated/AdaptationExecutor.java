package net.mdistributedmonitoring.statechartgenerator.adative.generated;

import net.mdistributedmonitoring.statechartgenerator.adative.MonitoringAdaptationManager;
import java.util.function.*;
import net.mdistributedmonitoring.statechartgenerator.adative.MonitoringAdaptationManager.Scope;
import net.mdistributedmonitoring.statechartgenerator.adative.IURule;
import net.mdistributedmonitoring.statechartgenerator.adative.AbstractURule;
import net.mdistributedmonitoring.statechartgenerator.adative.resolve.RuleResolver;
import java.util.*;
import net.mv.logging.ILogger;
import net.mv.logging.LoggerProvider;

public class AdaptationExecutor {

	private final Map<String, String> triggerMapping = new HashMap<>();
	private static final ILogger LOGGER = LoggerProvider.getLogger(AdaptationExecutor.class);
	private boolean inErrorState = false;
	private final ArrayList<IURule> globalRuleList = new ArrayList<>();

	public AdaptationExecutor() {
		initTriggers();
		initUbiquitousRules();
	}

	public void executeTrigger(String trigger) {
		LOGGER.info("Trigger received " + trigger);
		if (triggerMapping.containsKey(trigger)) {
			String func = triggerMapping.get(trigger);
			LOGGER.info("Executing trigger " + trigger);
			if (!inErrorState) {
				LOGGER.info("Executing Rule for " + trigger);
				execute.apply(func).get();
			} else {
				LOGGER.info("In Error State! - not executing Rule for " + trigger);
			}
		}
	}

	public void executeErrorTrigger(String trigger) {
		LOGGER.info("Error Trigger received " + trigger);
		inErrorState = true;
		RuleResolver.executeErrorTrigger(trigger, globalRuleList);
	}

	public void leaveErrorState(String name, String currentState) {
		LOGGER.info("Exit Error State - Transition to " + currentState);
		inErrorState = false;
		executeTrigger(currentState);
	}

	public void initTriggers() {
		triggerMapping.put("active", "active");
		triggerMapping.put("calibrating", "calibrating");
		triggerMapping.put("ready", "ready");
		triggerMapping.put("in_home_zone", "in_home_zone");
		triggerMapping.put("travelling_to_target_location", "travelling_to_target_location");
		triggerMapping.put("enter_target_zone", "enter_target_zone");
		triggerMapping.put("target_location_reached", "target_location_reached");
		triggerMapping.put("waiting_for_pickup", "waiting_for_pickup");
		triggerMapping.put("return_to_home_location", "return_to_home_location");
	}

	public void initUbiquitousRules() {
		{
			IURule r = new AbstractURule("Low Battery[]") {
				public void execute() {
					MonitoringAdaptationManager.getInstance().changeFrequency("Bot.BatteryStatus", 1);
					MonitoringAdaptationManager.getInstance().changeScope("Bot.BatteryStatus", Scope.CENTRAL);
					MonitoringAdaptationManager.getInstance().changeFrequency("Bot.Velocity", 5);
					MonitoringAdaptationManager.getInstance().changeScope("Bot.Velocity", Scope.CENTRAL);
					MonitoringAdaptationManager.getInstance().changeFrequency("Bot.JointState", 0);
					MonitoringAdaptationManager.getInstance().changeFrequency("Bot.Diagnostics", 5);
					MonitoringAdaptationManager.getInstance().changeScope("Bot.Diagnostics", Scope.CENTRAL);
					MonitoringAdaptationManager.getInstance().changeFrequency("Bot.MagneticField", 0);
					MonitoringAdaptationManager.getInstance().changeFrequency("Bot.VersionInfo", 0);
					MonitoringAdaptationManager.getInstance().changeFrequency("Bot.Odometry", 0);
					MonitoringAdaptationManager.getInstance().changeFrequency("Bot.SensorState", 0);
					MonitoringAdaptationManager.getInstance().changeFrequency("Bot.LaserScan", 0);
				}
			};
			globalRuleList.add(r);
			r.setName("battery");
			r.setSalience(22);
		}
		{
			IURule r = new AbstractURule("Diagnostic Error[]") {
				public void execute() {
					MonitoringAdaptationManager.getInstance().changeFrequency("Bot.BatteryStatus", 1);
					MonitoringAdaptationManager.getInstance().changeScope("Bot.BatteryStatus", Scope.CENTRAL);
					MonitoringAdaptationManager.getInstance().changeFrequency("Bot.Velocity", 1);
					MonitoringAdaptationManager.getInstance().changeScope("Bot.Velocity", Scope.CENTRAL);
					MonitoringAdaptationManager.getInstance().changeFrequency("Bot.JointState", 1);
					MonitoringAdaptationManager.getInstance().changeScope("Bot.JointState", Scope.CENTRAL);
					MonitoringAdaptationManager.getInstance().changeFrequency("Bot.Diagnostics", 1);
					MonitoringAdaptationManager.getInstance().changeScope("Bot.Diagnostics", Scope.CENTRAL);
					MonitoringAdaptationManager.getInstance().changeFrequency("Bot.MagneticField", 1);
					MonitoringAdaptationManager.getInstance().changeScope("Bot.MagneticField", Scope.CENTRAL);
					MonitoringAdaptationManager.getInstance().changeFrequency("Bot.VersionInfo", 3);
					MonitoringAdaptationManager.getInstance().changeScope("Bot.VersionInfo", Scope.CENTRAL);
					MonitoringAdaptationManager.getInstance().changeFrequency("Bot.Odometry", 1);
					MonitoringAdaptationManager.getInstance().changeScope("Bot.Odometry", Scope.CENTRAL);
					MonitoringAdaptationManager.getInstance().changeFrequency("Bot.SensorState", 1);
					MonitoringAdaptationManager.getInstance().changeScope("Bot.SensorState", Scope.CENTRAL);
					MonitoringAdaptationManager.getInstance().changeFrequency("Bot.LaserScan", 1);
					MonitoringAdaptationManager.getInstance().changeScope("Bot.LaserScan", Scope.CENTRAL);
				}
			};
			globalRuleList.add(r);
			r.setName("diagnostic");
			r.setSalience(22);
		}
	}private Function<String,Supplier<Boolean>>execute = performAdaptation -> {
	switch (performAdaptation) { case "active":
			return () -> {
			MonitoringAdaptationManager.getInstance().notifyRuleChange("active");
MonitoringAdaptationManager.getInstance().changeFrequency("Bot.BatteryStatus",1);MonitoringAdaptationManager.getInstance().changeScope("Bot.BatteryStatus",Scope.CENTRAL);MonitoringAdaptationManager.getInstance().changeFrequency("Bot.Diagnostics",1);MonitoringAdaptationManager.getInstance().changeScope("Bot.Diagnostics",Scope.CENTRAL);MonitoringAdaptationManager.getInstance().changeFrequency("Bot.JointState",5);MonitoringAdaptationManager.getInstance().changeScope("Bot.JointState",Scope.CENTRAL);MonitoringAdaptationManager.getInstance().changeFrequency("Bot.MagneticField",5);MonitoringAdaptationManager.getInstance().changeScope("Bot.MagneticField",Scope.CENTRAL);MonitoringAdaptationManager.getInstance().changeFrequency("Bot.VersionInfo",3);MonitoringAdaptationManager.getInstance().changeScope("Bot.VersionInfo",Scope.CENTRAL);MonitoringAdaptationManager.getInstance().changeFrequency("Bot.Odometry",5);MonitoringAdaptationManager.getInstance().changeScope("Bot.Odometry",Scope.CENTRAL);MonitoringAdaptationManager.getInstance().changeFrequency("Bot.SensorState",5);MonitoringAdaptationManager.getInstance().changeScope("Bot.SensorState",Scope.CENTRAL);MonitoringAdaptationManager.getInstance().changeFrequency("Bot.LaserScan",5);MonitoringAdaptationManager.getInstance().changeScope("Bot.LaserScan",Scope.CENTRAL);return true;}; case "calibrating":
			return () -> {
			MonitoringAdaptationManager.getInstance().notifyRuleChange("calibrating");
MonitoringAdaptationManager.getInstance().changeFrequency("Bot.BatteryStatus",5);MonitoringAdaptationManager.getInstance().changeScope("Bot.BatteryStatus",Scope.CENTRAL);MonitoringAdaptationManager.getInstance().changeFrequency("Bot.Diagnostics",10);MonitoringAdaptationManager.getInstance().changeScope("Bot.Diagnostics",Scope.CENTRAL);MonitoringAdaptationManager.getInstance().changeFrequency("Bot.Velocity",1);MonitoringAdaptationManager.getInstance().changeScope("Bot.Velocity",Scope.CENTRAL);MonitoringAdaptationManager.getInstance().changeFrequency("Bot.JointState",5);MonitoringAdaptationManager.getInstance().changeScope("Bot.JointState",Scope.CENTRAL);MonitoringAdaptationManager.getInstance().changeFrequency("Bot.MagneticField",1);MonitoringAdaptationManager.getInstance().changeScope("Bot.MagneticField",Scope.CENTRAL);MonitoringAdaptationManager.getInstance().changeFrequency("Bot.VersionInfo",10);MonitoringAdaptationManager.getInstance().changeScope("Bot.VersionInfo",Scope.CENTRAL);MonitoringAdaptationManager.getInstance().changeFrequency("Bot.Odometry",1);MonitoringAdaptationManager.getInstance().changeScope("Bot.Odometry",Scope.CENTRAL);MonitoringAdaptationManager.getInstance().changeFrequency("Bot.SensorState",1);MonitoringAdaptationManager.getInstance().changeScope("Bot.SensorState",Scope.CENTRAL);MonitoringAdaptationManager.getInstance().changeFrequency("Bot.LaserScan",1);MonitoringAdaptationManager.getInstance().changeScope("Bot.LaserScan",Scope.CENTRAL);return true;}; case "ready":
			return () -> {
			MonitoringAdaptationManager.getInstance().notifyRuleChange("ready");
MonitoringAdaptationManager.getInstance().changeFrequency("Bot.BatteryStatus",5);MonitoringAdaptationManager.getInstance().changeScope("Bot.BatteryStatus",Scope.CENTRAL);MonitoringAdaptationManager.getInstance().changeFrequency("Bot.Velocity",1);MonitoringAdaptationManager.getInstance().changeScope("Bot.Velocity",Scope.CENTRAL);MonitoringAdaptationManager.getInstance().changeFrequency("Bot.JointState",5);MonitoringAdaptationManager.getInstance().changeScope("Bot.JointState",Scope.CENTRAL);MonitoringAdaptationManager.getInstance().changeFrequency("Bot.Diagnostics",1);MonitoringAdaptationManager.getInstance().changeScope("Bot.Diagnostics",Scope.CENTRAL);MonitoringAdaptationManager.getInstance().changeFrequency("Bot.MagneticField",5);MonitoringAdaptationManager.getInstance().changeScope("Bot.MagneticField",Scope.CENTRAL);MonitoringAdaptationManager.getInstance().changeFrequency("Bot.VersionInfo",5);MonitoringAdaptationManager.getInstance().changeScope("Bot.VersionInfo",Scope.CENTRAL);MonitoringAdaptationManager.getInstance().changeFrequency("Bot.Odometry",5);MonitoringAdaptationManager.getInstance().changeScope("Bot.Odometry",Scope.CENTRAL);MonitoringAdaptationManager.getInstance().changeFrequency("Bot.SensorState",5);MonitoringAdaptationManager.getInstance().changeScope("Bot.SensorState",Scope.CENTRAL);MonitoringAdaptationManager.getInstance().changeFrequency("Bot.LaserScan",5);MonitoringAdaptationManager.getInstance().changeScope("Bot.LaserScan",Scope.CENTRAL);return true;}; case "in_home_zone":
			return () -> {
			MonitoringAdaptationManager.getInstance().notifyRuleChange("in_home_zone");
MonitoringAdaptationManager.getInstance().changeFrequency("Bot.BatteryStatus",3);MonitoringAdaptationManager.getInstance().changeScope("Bot.BatteryStatus",Scope.CENTRAL);MonitoringAdaptationManager.getInstance().changeFrequency("Bot.Velocity",2);MonitoringAdaptationManager.getInstance().changeScope("Bot.Velocity",Scope.CENTRAL);MonitoringAdaptationManager.getInstance().changeFrequency("Bot.JointState",2);MonitoringAdaptationManager.getInstance().changeScope("Bot.JointState",Scope.CENTRAL);MonitoringAdaptationManager.getInstance().changeFrequency("Bot.Diagnostics",10);MonitoringAdaptationManager.getInstance().changeScope("Bot.Diagnostics",Scope.CENTRAL);MonitoringAdaptationManager.getInstance().changeFrequency("Bot.MagneticField",10);MonitoringAdaptationManager.getInstance().changeScope("Bot.MagneticField",Scope.CENTRAL);MonitoringAdaptationManager.getInstance().changeFrequency("Bot.VersionInfo",10);MonitoringAdaptationManager.getInstance().changeScope("Bot.VersionInfo",Scope.CENTRAL);MonitoringAdaptationManager.getInstance().changeFrequency("Bot.Odometry",1);MonitoringAdaptationManager.getInstance().changeScope("Bot.Odometry",Scope.CENTRAL);MonitoringAdaptationManager.getInstance().changeFrequency("Bot.SensorState",10);MonitoringAdaptationManager.getInstance().changeScope("Bot.SensorState",Scope.CENTRAL);MonitoringAdaptationManager.getInstance().changeFrequency("Bot.LaserScan",10);MonitoringAdaptationManager.getInstance().changeScope("Bot.LaserScan",Scope.CENTRAL);return true;}; case "travelling_to_target_location":
			return () -> {
			MonitoringAdaptationManager.getInstance().notifyRuleChange("travelling_to_target_location");
MonitoringAdaptationManager.getInstance().changeFrequency("Bot.BatteryStatus",2);MonitoringAdaptationManager.getInstance().changeScope("Bot.BatteryStatus",Scope.CENTRAL);MonitoringAdaptationManager.getInstance().changeFrequency("Bot.Velocity",1);MonitoringAdaptationManager.getInstance().changeScope("Bot.Velocity",Scope.CENTRAL);MonitoringAdaptationManager.getInstance().changeFrequency("Bot.JointState",1);MonitoringAdaptationManager.getInstance().changeScope("Bot.JointState",Scope.CENTRAL);MonitoringAdaptationManager.getInstance().changeFrequency("Bot.Diagnostics",5);MonitoringAdaptationManager.getInstance().changeScope("Bot.Diagnostics",Scope.CENTRAL);MonitoringAdaptationManager.getInstance().changeFrequency("Bot.MagneticField",10);MonitoringAdaptationManager.getInstance().changeScope("Bot.MagneticField",Scope.CENTRAL);MonitoringAdaptationManager.getInstance().changeFrequency("Bot.VersionInfo",10);MonitoringAdaptationManager.getInstance().changeScope("Bot.VersionInfo",Scope.CENTRAL);MonitoringAdaptationManager.getInstance().changeFrequency("Bot.Odometry",1);MonitoringAdaptationManager.getInstance().changeScope("Bot.Odometry",Scope.CENTRAL);MonitoringAdaptationManager.getInstance().changeFrequency("Bot.SensorState",10);MonitoringAdaptationManager.getInstance().changeScope("Bot.SensorState",Scope.CENTRAL);MonitoringAdaptationManager.getInstance().changeFrequency("Bot.LaserScan",1);MonitoringAdaptationManager.getInstance().changeScope("Bot.LaserScan",Scope.CENTRAL);return true;}; case "enter_target_zone":
			return () -> {
			MonitoringAdaptationManager.getInstance().notifyRuleChange("enter_target_zone");
MonitoringAdaptationManager.getInstance().changeFrequency("Bot.BatteryStatus",1);MonitoringAdaptationManager.getInstance().changeScope("Bot.BatteryStatus",Scope.CENTRAL);MonitoringAdaptationManager.getInstance().changeFrequency("Bot.JointState",5);MonitoringAdaptationManager.getInstance().changeScope("Bot.JointState",Scope.CENTRAL);MonitoringAdaptationManager.getInstance().changeFrequency("Bot.Diagnostics",2);MonitoringAdaptationManager.getInstance().changeScope("Bot.Diagnostics",Scope.CENTRAL);return true;}; case "target_location_reached":
			return () -> {
			MonitoringAdaptationManager.getInstance().notifyRuleChange("target_location_reached");
MonitoringAdaptationManager.getInstance().changeFrequency("Bot.BatteryStatus",5);MonitoringAdaptationManager.getInstance().changeScope("Bot.BatteryStatus",Scope.CENTRAL);MonitoringAdaptationManager.getInstance().changeFrequency("Bot.Velocity",5);MonitoringAdaptationManager.getInstance().changeScope("Bot.Velocity",Scope.CENTRAL);MonitoringAdaptationManager.getInstance().changeFrequency("Bot.JointState",2);MonitoringAdaptationManager.getInstance().changeScope("Bot.JointState",Scope.CENTRAL);MonitoringAdaptationManager.getInstance().changeFrequency("Bot.Diagnostics",2);MonitoringAdaptationManager.getInstance().changeScope("Bot.Diagnostics",Scope.CENTRAL);MonitoringAdaptationManager.getInstance().changeFrequency("Bot.Odometry",5);MonitoringAdaptationManager.getInstance().changeScope("Bot.Odometry",Scope.CENTRAL);MonitoringAdaptationManager.getInstance().changeFrequency("Bot.SensorState",5);MonitoringAdaptationManager.getInstance().changeScope("Bot.SensorState",Scope.CENTRAL);MonitoringAdaptationManager.getInstance().changeFrequency("Bot.LaserScan",5);MonitoringAdaptationManager.getInstance().changeScope("Bot.LaserScan",Scope.CENTRAL);return true;}; case "waiting_for_pickup":
			return () -> {
			MonitoringAdaptationManager.getInstance().notifyRuleChange("waiting_for_pickup");
MonitoringAdaptationManager.getInstance().changeFrequency("Bot.BatteryStatus",10);MonitoringAdaptationManager.getInstance().changeScope("Bot.BatteryStatus",Scope.CENTRAL);MonitoringAdaptationManager.getInstance().changeFrequency("Bot.Velocity",10);MonitoringAdaptationManager.getInstance().changeScope("Bot.Velocity",Scope.CENTRAL);MonitoringAdaptationManager.getInstance().changeFrequency("Bot.JointState",10);MonitoringAdaptationManager.getInstance().changeScope("Bot.JointState",Scope.CENTRAL);MonitoringAdaptationManager.getInstance().changeFrequency("Bot.Diagnostics",10);MonitoringAdaptationManager.getInstance().changeScope("Bot.Diagnostics",Scope.CENTRAL);MonitoringAdaptationManager.getInstance().changeFrequency("Bot.MagneticField",10);MonitoringAdaptationManager.getInstance().changeScope("Bot.MagneticField",Scope.CENTRAL);MonitoringAdaptationManager.getInstance().changeFrequency("Bot.VersionInfo",10);MonitoringAdaptationManager.getInstance().changeScope("Bot.VersionInfo",Scope.CENTRAL);MonitoringAdaptationManager.getInstance().changeFrequency("Bot.Odometry",1);MonitoringAdaptationManager.getInstance().changeScope("Bot.Odometry",Scope.CENTRAL);MonitoringAdaptationManager.getInstance().changeFrequency("Bot.SensorState",1);MonitoringAdaptationManager.getInstance().changeScope("Bot.SensorState",Scope.CENTRAL);MonitoringAdaptationManager.getInstance().changeFrequency("Bot.LaserScan",1);MonitoringAdaptationManager.getInstance().changeScope("Bot.LaserScan",Scope.CENTRAL);return true;}; case "return_to_home_location":
			return () -> {
			MonitoringAdaptationManager.getInstance().notifyRuleChange("return_to_home_location");
MonitoringAdaptationManager.getInstance().changeFrequency("Bot.BatteryStatus",2);MonitoringAdaptationManager.getInstance().changeScope("Bot.BatteryStatus",Scope.CENTRAL);MonitoringAdaptationManager.getInstance().changeFrequency("Bot.Velocity",1);MonitoringAdaptationManager.getInstance().changeScope("Bot.Velocity",Scope.CENTRAL);MonitoringAdaptationManager.getInstance().changeFrequency("Bot.JointState",1);MonitoringAdaptationManager.getInstance().changeScope("Bot.JointState",Scope.CENTRAL);MonitoringAdaptationManager.getInstance().changeFrequency("Bot.Diagnostics",1);MonitoringAdaptationManager.getInstance().changeScope("Bot.Diagnostics",Scope.CENTRAL);MonitoringAdaptationManager.getInstance().changeFrequency("Bot.MagneticField",2);MonitoringAdaptationManager.getInstance().changeScope("Bot.MagneticField",Scope.CENTRAL);MonitoringAdaptationManager.getInstance().changeFrequency("Bot.Odometry",1);MonitoringAdaptationManager.getInstance().changeScope("Bot.Odometry",Scope.CENTRAL);MonitoringAdaptationManager.getInstance().changeFrequency("Bot.SensorState",1);MonitoringAdaptationManager.getInstance().changeScope("Bot.SensorState",Scope.CENTRAL);MonitoringAdaptationManager.getInstance().changeFrequency("Bot.LaserScan",1);MonitoringAdaptationManager.getInstance().changeScope("Bot.LaserScan",Scope.CENTRAL);return true;};}LOGGER.error("Trigger not found: " + performAdaptation);return null;};private Function<String,Supplier<Boolean>>execute_error = performAdaptation -> {
	switch (performAdaptation) {}LOGGER.error("Trigger not found: " + performAdaptation);return null;};}