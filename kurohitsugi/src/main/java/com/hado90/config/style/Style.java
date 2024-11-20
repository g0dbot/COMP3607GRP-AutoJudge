package com.hado90.config.style;

import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hado90.config.Config;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;

/**
 * Style class implements the Config interface to manage configuration settings
 * related to style. The class loads configurations from a JSON file and provides
 * methods to get and set style-specific configuration values.
 */
public class Style implements Config {

    /**
     * A map that holds the configuration values with their corresponding StyleKey.
     * The map is made unmodifiable after the config is loaded to prevent further changes.
     */
    private static Map<StyleKey, String> configValues = new HashMap<>();

    /**
     * Loads configuration values from the specified JSON file. The file is expected
     * to contain key-value pairs where keys correspond to {@link StyleKey} enum values.
     * 
     * @param configFilePath the relative path to the JSON configuration file.
     * @throws FileNotFoundException if the specified configuration file cannot be found.
     */
    @Override
    public void loadConfig(String configFilePath) {
        ObjectMapper objectMapper = new ObjectMapper();

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(configFilePath)) {
            if (inputStream == null) {
                throw new FileNotFoundException("Resource not found: " + configFilePath);
            }

            @SuppressWarnings("unchecked")
            Map<String, String> loadedConfigValues = objectMapper.readValue(inputStream, Map.class);

            // Process each entry and map the key to a ConfigStyleKey enum if it exists
            for (Map.Entry<String, String> entry : loadedConfigValues.entrySet()) {
                try {
                    StyleKey key = StyleKey.valueOf(entry.getKey());
                    configValues.put(key, entry.getValue());
                } catch (IllegalArgumentException e) {
                    // Handle the case when the JSON key does not match any ConfigStyleKey enum value
                    System.err.println("Unknown config key: " + entry.getKey() + ". Skipping...");
                }
            }

            // Make configValues unmodifiable to prevent further modifications
            configValues = Collections.unmodifiableMap(configValues);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves the configuration value associated with the specified key.
     * The key is expected to be one of the defined {@link StyleKey} enum values.
     *
     * @param key the key to retrieve the configuration value for.
     * @return the configuration value associated with the key, or null if the key is invalid.
     */
    public static String getConfigValue(String key) {
        try {
            StyleKey styleKey = StyleKey.valueOf(key); // Convert String to StyleKey
            return configValues.get(styleKey);         // Retrieve value using the key
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid key: " + key);
            return null; // Return null if the key is invalid
        }
    }

    /**
     * Sets a configuration value for the specified key. The key must already exist in
     * the configuration, as new keys cannot be added after the configuration has been loaded.
     *
     * @param key   the key for which the value should be set.
     * @param value the new value to set for the configuration key.
     * @throws UnsupportedOperationException if attempting to add a new key to the configuration.
     * @throws IllegalArgumentException if the key is invalid.
     */
    @Override
    public void setConfigValue(String key, String value) {
        try {
            StyleKey styleKey = StyleKey.valueOf(key); // Convert String to StyleKey
            if (!configValues.containsKey(styleKey)) {
                throw new UnsupportedOperationException("Cannot add new keys to config");
            }
            configValues.put(styleKey, value); // Update the value
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid key: " + key, e);
        }
    }
}
