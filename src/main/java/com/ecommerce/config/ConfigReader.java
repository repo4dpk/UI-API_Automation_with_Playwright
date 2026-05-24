package com.ecommerce.config;

import com.ecommerce.utils.LoggerUtil;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Singleton configuration reader that loads environment-specific properties.
 * Supports multi-environment configuration (config, staging, production).
 */
public class ConfigReader {

    private static ConfigReader instance;
    private final Properties properties;

    private ConfigReader() {
        properties = new Properties();
        String env = System.getProperty("env", "config");
        String configFile = "config/" + env + ".properties";

        LoggerUtil.info("Loading configuration from: " + configFile);

        try (InputStream input = getClass().getClassLoader()
                .getResourceAsStream(configFile)) {
            if (input == null) {
                throw new RuntimeException(
                    "Configuration file not found: " + configFile
                );
            }
            properties.load(input);
            LoggerUtil.info("Configuration loaded successfully for env: " + env);
        } catch (IOException e) {
            throw new RuntimeException(
                "Failed to load configuration: " + configFile, e
            );
        }
    }

    public static synchronized ConfigReader getInstance() {
        if (instance == null) {
            instance = new ConfigReader();
        }
        return instance;
    }

    public String getProperty(String key) {
        String value = System.getProperty(key);
        if (value != null) {
            return value;
        }
        value = properties.getProperty(key);
        if (value == null) {
            LoggerUtil.warn("Property not found: " + key);
        }
        return value;
    }

    public String getProperty(String key, String defaultValue) {
        String value = getProperty(key);
        return value != null ? value : defaultValue;
    }

    public String getBaseUrl() {
        return getProperty("base.url");
    }

    public String getApiBaseUrl() {
        return getProperty("api.base.url");
    }

    public String getApiKey(){
        return getProperty("api.key");
    }

    public String getBrowser() {
        return getProperty("browser", "chromium");
    }

    public boolean isHeadless() {
        return Boolean.parseBoolean(getProperty("headless", "true"));
    }

    public int getDefaultTimeout() {
        return Integer.parseInt(getProperty("default.timeout", "30000"));
    }

    public double getSlowMo() {
        return Double.parseDouble(getProperty("slow.mo", "0"));
    }

    public boolean isTracingEnabled() {
        return Boolean.parseBoolean(getProperty("tracing.enabled", "false"));
    }

    /**
     * Reset the singleton instance - useful for re-initializing
     * configuration in tests.
     */
    public static synchronized void reset() {
        instance = null;
    }
}
