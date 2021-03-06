package com.scispike.effectivejava.solution.ch01;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ConfigDepot {

	private static Map<String, Map<String, String>> configs = new HashMap<String, Map<String, String>>();
	
	public static Map<String, String> getConfig(String configFileName) {
		Map<String, String> config = null;
	
		synchronized (configs) {
			config = configs.get(configFileName);
			if (config == null) {
				config = getConfigFromFileOnClassPaths(configFileName);
				configs.put(configFileName, config);
			}
		}
		
		return config;
	}
	
	private static Map<String, String> getConfigFromFileOnClassPaths(String configFileName) {
		Map<String, String> rslt = null;
		Properties properties = null;		
		InputStream is = null;
		
		try {
			is = ConfigDepot.class.getClassLoader().getResourceAsStream(configFileName);
			if (is != null) {
				properties = new Properties();
				properties.load(is);				
			}
		} 
		catch (IOException e) {
			System.err.println(e);
		}			
		finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					System.err.println(e);
				}
			}
		}
				
		if (properties != null) 
			rslt = new HashMap<String, String>((Map)properties);
		
		return rslt;
	}
	
	public static void main(String[] args) {
		Map<String, String> devConfig = ConfigDepot.getConfig("dev.properties");
		System.out.printf("Development mode configurations:\n\t%s\n", devConfig);
		System.out.printf("Development mode host:\n\t%s\n", devConfig.get("host"));
		Map<String, String> prodConfig = ConfigDepot.getConfig("prod.properties");		
		System.out.printf("Production mode configurations:\n\t%s\n", prodConfig);
		System.out.printf("Production mode host:\n\t%s\n", prodConfig.get("host"));
		
	}
}
