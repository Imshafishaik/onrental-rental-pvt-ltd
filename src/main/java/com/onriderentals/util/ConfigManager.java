package com.onriderentals.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {
    private static final Properties properties = new Properties();
    
    static {
        try {
            InputStream input = ConfigManager.class.getClassLoader()
                    .getResourceAsStream("database.properties");
            if (input == null) {
                System.err.println("Unable to find database.properties file");
            } else {
                properties.load(input);
                input.close();
            }
        } catch (IOException ex) {
            System.err.println("Error loading database.properties: " + ex.getMessage());
        }
    }
    
    public static String get(String key) {
        return properties.getProperty(key);
    }
    
    public static String get(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
    
    public static int getInt(String key, int defaultValue) {
        String value = properties.getProperty(key);
        if (value == null) return defaultValue;
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
