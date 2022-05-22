package com.env.resources;

import com.util.cloud.ConfigurationManager;

/**
 * Application external resources
 */
@SuppressWarnings("unchecked")
public enum JobResources {


    RECURRENT_JOB {
        public String value() {
            return System.getenv().getOrDefault("RECURRENT_JOB", configuration.getPropertyAsString("scheduled.job.recurrent.trigger"));
        }
    };
    private static final com.util.cloud.Configuration configuration = ConfigurationManager.getConfiguration();

    public abstract <T> T value();

}
