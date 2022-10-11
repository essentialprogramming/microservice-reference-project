package com.util.micrometer;

import com.util.exceptions.ErrorCodes;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.search.RequiredSearch;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.spy;

public class ErrorMetricsTest {

    @Spy
    private final MeterRegistry meterRegistry = new SimpleMeterRegistry();

    @BeforeEach
    void setUp() {
        ErrorCodes.registerErrorCodes(ErrorCode.class);
    }

    @Test
    public void increment_error_metric() {
        final ErrorMetrics errorMetrics = new ErrorMetrics(meterRegistry);
        final RequiredSearch requiredSearch = meterRegistry.get(ErrorMetrics.METRIC_KEY)
                .tag(ErrorMetrics.METRIC_ERROR_ID_TAG, ErrorCode.INTERNAL_ERROR.getCode());
        final Counter counter = requiredSearch.counter();
        assertEquals(0.0, counter.count(), 0);
        errorMetrics.increment(ErrorCode.INTERNAL_ERROR.getCode());
        assertEquals(1.0, counter.count(), 0);
    }

    @Test
    public void increment_unknown_error_metric() {
        final ErrorMetrics errorMetrics = new ErrorMetrics(meterRegistry);
        final RequiredSearch requiredSearch = meterRegistry.get(ErrorMetrics.METRIC_KEY)
                .tag(ErrorMetrics.METRIC_ERROR_ID_TAG, ErrorMetrics.UNKNOWN_ERROR_ID);
        final Counter counter = requiredSearch.counter();
        assertEquals(0.0, counter.count(), 0);
        errorMetrics.increment("ERROR007");
        assertEquals(1.0, counter.count(), 0);
    }


    private enum ErrorCode implements ErrorCodes.ErrorCode {

        INTERNAL_ERROR("INTERNAL-ERR001", "Error");

        private final String code;
        private final String description;


        /**
         * The standard constructor.
         *
         * @param code        the error code
         * @param description the error description
         */
        ErrorCode(final String code, final String description) {
            this.code = code;
            this.description = description;
        }

        public String getCode() {
            return this.code;
        }

        public String getDescription() {
            return description;
        }

    }
}
