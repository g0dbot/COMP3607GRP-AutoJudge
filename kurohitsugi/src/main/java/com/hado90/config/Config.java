package com.hado90.config;

/**
 * Interface defining methods for configuration management.
 */
public interface Config {

    /**
     * Loads the configuration from the specified file.
     *
     * @param configFilePath the path to the configuration file
     */
    void loadConfig(String configFilePath);

    /**
     * Retrieves the value of a configuration key.
     * 
     * @param key the key of the configuration
     * @return the value of the configuration as a string
     */
    static String getConfigValue(String key) {
        return null;
    }

    /**
     * Updates the value of a configuration key.
     *
     * @param key the key of the configuration
     * @param value the new value to set
     */
    void setConfigValue(String key, String value);
}