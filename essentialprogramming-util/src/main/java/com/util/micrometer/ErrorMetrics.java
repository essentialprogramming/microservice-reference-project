package com.util.micrometer;

import com.util.exceptions.ErrorCodes;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Component to realise custom error metrics.
 */
public class ErrorMetrics {

    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorMetrics.class);

    protected static final String UNKNOWN_ERROR_ID = "unknown_errorId";
    protected static final String METRIC_KEY = "application:errors";
    protected static final String METRIC_ERROR_ID_TAG = "errorId";

    private final Map<String, Counter> counters;

    public ErrorMetrics(final MeterRegistry meterRegistry) {
        counters = new ConcurrentHashMap<>();
        final List<String> errorIds =
                new ArrayList<>(ErrorCodes.getMessages().keySet());

        counters.putAll(errorIds.stream().collect(Collectors.toMap(errorId -> errorId,
                errorId -> Counter.builder(METRIC_KEY).tag(METRIC_ERROR_ID_TAG, errorId)
                        .description("count of error log calls with error id " + errorId).register(meterRegistry))));
        counters.put(UNKNOWN_ERROR_ID,
                Counter.builder(METRIC_KEY).tag(METRIC_ERROR_ID_TAG, UNKNOWN_ERROR_ID)
                        .description("count of error log calls with unknown error id").register(meterRegistry));
    }

    /**
     * Increment number of errors for given error ID.
     *
     * @param errorId error ID
     */
    public void increment(final String errorId) {
        if (errorId != null && counters.containsKey(errorId)) {
            counters.get(errorId).increment();
        } else {
            counters.get(UNKNOWN_ERROR_ID).increment();
            LOGGER.warn("Unexpected error id '{}' has been logged!", errorId);
        }
    }

}