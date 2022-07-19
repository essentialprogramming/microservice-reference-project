package com.config;

import com.util.async.ExecutorsProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;


@Component
public class StartupApplicationListener implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger log = LoggerFactory.getLogger(StartupApplicationListener.class);


    public StartupApplicationListener() {
    }

    public void onApplicationEvent(@NonNull ContextRefreshedEvent event) {
        log.info("Application started..");

        Runtime.getRuntime().addShutdownHook(new Thread( ()
                -> ExecutorsProvider.getManagedExecutorService().stop(), "appShutdownHook")
        );

    }
}