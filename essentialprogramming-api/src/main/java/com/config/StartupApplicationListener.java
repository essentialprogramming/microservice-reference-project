package com.config;

import com.api.exceptions.codes.ErrorCode;
import com.util.async.ExecutorsProvider;
import com.util.exceptions.ErrorCodes;
import com.util.micrometer.ErrorMetricsAppender;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Collection;


@Component
public class StartupApplicationListener implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger log = LoggerFactory.getLogger(StartupApplicationListener.class);

    private final ErrorMetricsAppender errorMetricsAppender;

    public StartupApplicationListener(final ErrorMetricsAppender errorMetricsAppender) {
        this.errorMetricsAppender = errorMetricsAppender;
    }

    public void onApplicationEvent(@NonNull ContextRefreshedEvent event) {
        log.info("Application started..");

        Runtime.getRuntime().addShutdownHook(new Thread(()
                -> ExecutorsProvider.getManagedExecutorService().stop(), "appShutdownHook")
        );


        final LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        final LoggerConfig loggerConfig = ctx.getConfiguration().getRootLogger();

        loggerConfig.addAppender(
                errorMetricsAppender,
                Level.ERROR,
                errorMetricsAppender.getFilter()
        );

    }
}