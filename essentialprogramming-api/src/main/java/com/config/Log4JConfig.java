package com.config;

import com.util.cloud.DeploymentStrategy;
import com.util.io.FileInputResource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import java.io.IOException;
import java.net.URISyntaxException;

import static com.util.cloud.Environment.getProperty;


public class Log4JConfig {

    private static final String DEPLOYMENT_STRATEGY = "DEPLOYMENT_STRATEGY";
    private static final DeploymentStrategy deploymentStrategy = DeploymentStrategy
            .valueOf(getProperty(DEPLOYMENT_STRATEGY, "LOCAL"));

    private static final String LOG4J_CONFIG_FILE = "LOG4J_CONFIG_FILE";
    private static final String DEFAULT_LOG4J_CONFIG_FILE = "classpath:log4j2.cloud.xml";

    private static final String configFile = getProperty(LOG4J_CONFIG_FILE, DEFAULT_LOG4J_CONFIG_FILE);

    /**
     * This will force a reconfiguration.
     * See: https://people.apache.org/~rgoers/log4j2/faq.html#config_location
     */
    public static void configureLog4j() {

        final LoggerContext context = (LoggerContext) LogManager.getContext(false);

        if (DeploymentStrategy.CLOUD.equals(deploymentStrategy)){
            try (final FileInputResource fileInputResource = new FileInputResource(configFile)) {
                context.setConfigLocation(fileInputResource.getFile().toURI());
            } catch (IOException | URISyntaxException ignore) {
            }
        }
    }
}
