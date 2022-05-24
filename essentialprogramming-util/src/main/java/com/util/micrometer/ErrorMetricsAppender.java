package com.util.micrometer;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.filter.ThresholdFilter;


public class ErrorMetricsAppender extends AbstractAppender {

    private final ErrorMetrics errorMetrics;

    @SuppressWarnings("squid:S1699") // call start() method is correct in this constructor
    public ErrorMetricsAppender(final ErrorMetrics errorMetrics) {
        super("ErrorMetricsAppender",
                ThresholdFilter.createFilter(Level.ERROR, Filter.Result.ACCEPT, Filter.Result.DENY),
                null,  true, null );
        this.errorMetrics = errorMetrics;
        this.start();
    }

    /**
     * Processes log events incrementing counters
     */
    @Override
    public void append(final LogEvent logEvent) {
        errorMetrics.increment(logEvent.getContextData().getValue("errorID"));
    }
}

