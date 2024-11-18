package com.hado90.config;

public interface Config {
    void loadConfig(String configFilePath);
    static String getConfigValue(String key){return null;}
    void setConfigValue(String key, String value);
}
