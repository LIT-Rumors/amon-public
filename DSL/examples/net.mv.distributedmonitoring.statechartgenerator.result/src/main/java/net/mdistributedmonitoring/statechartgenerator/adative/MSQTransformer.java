package net.mdistributedmonitoring.statechartgenerator.adative;

import java.text.DateFormat;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class MSQTransformer {
	
	
	public static final transient Gson GSON = new GsonBuilder().enableComplexMapKeySerialization().serializeNulls()
			.setDateFormat(DateFormat.LONG).setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).setVersion(1.0)
			.serializeSpecialFloatingPointValues().serializeSpecialFloatingPointValues().create();

	public static String toJSONString(String... keyvalues) {
		if (keyvalues.length % 2 !=0) {
			throw new IllegalArgumentException("Must be key:value pairs");
		}
		Map<String,String> map = new HashMap<>();
		for (int i = 0; i < keyvalues.length; i+=2) {
			map.put(keyvalues[i], keyvalues[i+1]);
		}
		return GSON.toJson(map);
		
	}

}
