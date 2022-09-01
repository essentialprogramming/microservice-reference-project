package com.util.logging.wrapper;

import com.util.exceptions.ErrorCodes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.spi.ExtendedLogger;
import org.apache.logging.log4j.spi.ExtendedLoggerWrapper;

import static com.aventrix.jnanoid.jnanoid.NanoIdUtils.randomNanoId;
import static com.util.logging.LoggingKey.*;

public class MetricLogger implements Log {

    private final ExtendedLoggerWrapper log;

    public MetricLogger(final Class<?> clazz) {
        final Logger logger = LogManager.getLogger(clazz);
        log = new ExtendedLoggerWrapper((ExtendedLogger) logger, logger.getName(), logger.getMessageFactory());
    }

    public void error(final ErrorCodes.ErrorCode error) {
        ThreadContext.put(ERROR_ID.getValue(), error.getCode());
        this.log.error(error.getDescription());
        ThreadContext.clearMap();
    }

    public void info(final String message) {
        ThreadContext.put(INFO_ID.getValue(), "info_" + randomNanoId());
        this.log.info(message);
        ThreadContext.clearMap();
    }

    public void warn(final String message) {
        ThreadContext.put(WARN_ID.getValue(), "warn_" + randomNanoId());
        this.log.warn(message);
        ThreadContext.clearMap();
    }
}
