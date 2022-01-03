package com.util.cloud;

import com.util.io.FileInputResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class ConfigurationManager {

    private static final String CONFIG_FILE = "CONFIG_FILE";
    private static final String DEFAULT_CONFIG_FILE = "classpath:application.properties";

    private static final Logger logger = LoggerFactory.getLogger(ConfigurationManager.class);

    private ConfigurationManager() {
        throw new IllegalAccessError("Instantiation prohibited");
    }

    private static class ConfigurationHolder {
        private static final Configuration CONFIGURATION = loadConfiguration();
    }

    public static Configuration getConfiguration() {
        return ConfigurationHolder.CONFIGURATION;
    }

    /**
     * Load configuration from property file.
     */
    private static Configuration loadConfiguration() {

        final String configFile = Environment.getProperty(CONFIG_FILE, DEFAULT_CONFIG_FILE);

        FileConfiguration configuration;
        try (final FileInputResource fileInputResource = new FileInputResource(configFile);
             final InputStream stream = fileInputResource.getInputStream()) {

            final Properties properties = fetchProperties(stream);
            configuration = new FileConfiguration(properties);

        } catch (Exception e) {
            logger.error("Configuration could not be loaded.");
            throw new RuntimeException("Unable to load " + configFile, e);
        }
        return configuration;
    }

    private static Properties fetchProperties(final InputStream stream) throws IOException {
        final Properties properties = new Properties();
        properties.load(stream);
        return properties;
    }

}
