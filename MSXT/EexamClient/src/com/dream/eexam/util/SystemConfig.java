package com.dream.eexam.util;

import java.io.InputStream;
import java.util.Properties;

public class SystemConfig {
	private final static String PROPERTY_FILE_NAME = "config.properties";
	private static SystemConfig conf = null;
	private Properties agproperties = null;
	
	public SystemConfig(String propFile) {
        try {
            agproperties = new Properties();
            InputStream in = this.getClass().getClassLoader().getResourceAsStream(propFile);
            agproperties.load(in);
            in.close();
        } catch (Exception ignored) {
        	
        }
	}

	public static synchronized SystemConfig getInstance() {
        if (conf == null) {
        	conf = new SystemConfig(PROPERTY_FILE_NAME);
        }
        return conf;
    }
	
    public String getPropertyValue(String key) {
        return agproperties.getProperty(key);
    }
}
