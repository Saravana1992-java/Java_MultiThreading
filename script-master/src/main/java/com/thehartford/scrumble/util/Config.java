package com.thehartford.scrumble.util;

import java.util.HashMap;
import java.util.stream.Collectors;

public class Config {

	private static Config config;
	private static HashMap<String,String> configMap;
	
	private Config() {
		configMap = new HashMap<String,String>();
		configMap.putAll(GeneralUtil.getProperties(GeneralConstants.CONFIG_FILE_PATH).entrySet().stream()
				.collect(Collectors.toMap(e -> e.getKey().toString(), e -> e.getValue().toString())));
	}

	public static Config getInstance() {
		if (config == null) {
			config = new Config();
		}
		return config;
	}
	
	public HashMap<String, String> getConfigMap() {
		return configMap;
	}
}
