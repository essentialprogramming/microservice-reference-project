package com.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;


@Component
public class ShutdownApplicationListener implements ApplicationListener<ContextClosedEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(ShutdownApplicationListener.class);


    public ShutdownApplicationListener() {
    }

    public void onApplicationEvent(@NonNull ContextClosedEvent event) {
        LOG.info("Application shutting down..");
    }
}