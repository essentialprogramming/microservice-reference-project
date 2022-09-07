package com.api.env.resources;

import com.util.cloud.ConfigurationManager;
import com.util.cloud.Environment;

/**
 * Application external resources
 */
@SuppressWarnings("unchecked")
public enum AppResources {

    /**
     * Url to be sent in the confirmation email.
     */
    ACCOUNT_CONFIRMATION_URL {
        public String value() {
            return Environment.getProperty("APP_URL", configuration.getPropertyAsString("app.url")) + "/confirm";
        }
    },
    /**
     * Base URL of the application;
     */
    APP_URL {
        public String value() {
            return Environment.getProperty("APP_URL", configuration.getPropertyAsString("app.url"));
        }
    },

    ENCRYPTION_KEY {
        public String value() {
            return Environment.getProperty("ENCRYPTION_KEY", configuration.getPropertyAsString("encryption.key"));
        }
    },

    DB_HOSTNAME {
        public String value() {
            return Environment.getProperty("DB_HOSTNAME", configuration.getPropertyAsString("db.hostname"));
        }
    },

    DB_USER {
        public String value() {
            return Environment.getProperty("DB_USER", configuration.getPropertyAsString("db.user"));
        }
    },

    DB_PASSWORD {
        public String value() {
            return Environment.getProperty("DB_PASSWORD", configuration.getPropertyAsString("db.password"));
        }
    },
    OTP_LOGIN_URL {
        public String value() {
            return Environment.getProperty("APP_URL", configuration.getPropertyAsString("app.url")) + "/otp-login";
        }
    },

    /**
     * Datadog configuration.
     */
    DATADOG_API_KEY {
        public String value() {
            return Environment.getProperty("DATADOG_API_KEY", configuration.getPropertyAsString("datadog.api.key"));
        }
    },

    DATADOG_APPLICATION_KEY {
        public String value() {
            return Environment.getProperty("DATADOG_APPLICATION_KEY", configuration.getPropertyAsString("datadog.application.key"));
        }
    },

    DATADOG_URI {
        public String value() {
            return Environment.getProperty("DATADOG_URI", configuration.getPropertyAsString("datadog.uri"));
        }
    },

    DATADOG_ENABLED {
        public Boolean value() {
            return Environment.getProperty("DATADOG_ENABLED", configuration.getPropertyAsBoolean("datadog.enabled"));
        }
    },

    DATADOG_STEP_SECONDS {
        public Long value() {
            return Environment.getProperty("DATADOG_STEP_SECONDS", configuration.getPropertyAsLong("datadog.step.seconds"));
        }
    },

    DATADOG_DESCRIPTION {
        public Boolean value() {
            return Environment.getProperty("DATADOG_DESCRIPTION", configuration.getPropertyAsBoolean("datadog.description"));

        }
    };
    private static final com.util.cloud.Configuration configuration = ConfigurationManager.getConfiguration();

    public abstract <T> T value();
	
    }
