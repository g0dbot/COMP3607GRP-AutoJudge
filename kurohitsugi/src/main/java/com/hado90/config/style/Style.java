package com.hado90.config.style;

import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hado90.config.Config;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;

public class Style implements Config {

    private static Map<StyleKey, String> configValues = new HashMap<>();

    //contract init
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
    
    public static String getConfigValue(String key) {
        try {
            StyleKey styleKey = StyleKey.valueOf(key); // Convert String to StyleKey
            return configValues.get(styleKey);         // Retrieve value using the key
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid key: " + key);
            return null; // Return null if the key is invalid
        }
    }

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
