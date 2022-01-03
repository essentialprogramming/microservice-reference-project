package com.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;


@Component
public class StartupApplicationListener implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(StartupApplicationListener.class);


    public StartupApplicationListener() {
    }

    public void onApplicationEvent(@NonNull ContextRefreshedEvent event) {
        LOG.info("Application started..");

    }
}