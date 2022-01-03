package com.util.cloud;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;


public class FileConfiguration implements Configuration {

    private static final Logger LOG = LoggerFactory.getLogger(FileConfiguration.class);

    private final Properties properties;

    public FileConfiguration(final Properties properties) {
        this.properties = properties;
    }

    @Override
    public boolean isEmpty() {
        return this.properties.isEmpty();
    }

    @Override
    public boolean containsKey(String key) {
        return this.properties.containsKey(key);
    }

    @Override
    public Object getProperty(String key) {
        return this.properties.get(key);
    }

    @Override
    public void addProperty(String key, String value) {
        if (key != null && value != null) {
            this.properties.put(key, value);
        }
    }

    @Override
    public String getPropertyAsString(String key) {
        return this.properties.getProperty(key);
    }

    @Override
    public Integer getPropertyAsInteger(String key) {
        if (this.getProperty(key) == null) return null;

        final String value = this.properties.getProperty(key);
        final int property;
        try {
            property = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            LOG.error("Property with key '" + key + "' and value '" + value + "' can not be converted to int" + ".");
            throw new IllegalArgumentException("Property with key '" + key + "' and value '" + value + "' can not be converted to int" + ".", e);
        }

        return property;
    }


    @Override
    public void cleanProperties() {
        this.properties.clear();
    }

}
