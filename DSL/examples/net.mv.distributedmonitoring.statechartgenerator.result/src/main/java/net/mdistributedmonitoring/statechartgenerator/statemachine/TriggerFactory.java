package net.mdistributedmonitoring.statechartgenerator.statemachine;

import java.text.DateFormat;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class TriggerFactory {
	
	private static final String UBIQUITOS_STATE = "*UB*";
	
	private static final Gson GSON = new GsonBuilder().enableComplexMapKeySerialization().serializeNulls()
			.setDateFormat(DateFormat.LONG).setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_DASHES)
			.setVersion(1.0).serializeSpecialFloatingPointValues().serializeSpecialFloatingPointValues().create();
	
	
	public static EventTrigger getTrigger(String triggerMessage) {
		
		if(triggerMessage.contains(UBIQUITOS_STATE)) {
			return GSON.fromJson(triggerMessage, ErrorTrigger.class);
		}
		
		return GSON.fromJson(triggerMessage, EventTrigger.class);

		
	}
	
public static String TriggerToString(EventTrigger trigger) {
		

		return GSON.toJson(trigger);

		
	}
	

}
