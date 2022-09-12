package com.logging;

import org.apache.logging.log4j.CloseableThreadContext;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.ParameterizedMessage;
import org.apache.logging.log4j.message.SimpleMessage;
import org.apache.logging.log4j.spi.ExtendedLogger;
import org.apache.logging.log4j.status.StatusLogger;
import org.slf4j.Marker;
import org.slf4j.spi.LocationAwareLogger;

import java.util.Arrays;

import static com.logging.LoggingAttributes.*;
import static java.util.Arrays.copyOfRange;
import static org.apache.logging.log4j.Level.*;

public class Log4jLogger implements LocationAwareLogger {

    public static final String FQCN = Log4jLogger.class.getName();

    private final ExtendedLogger log4jLogger;
    private final String name;

    public Log4jLogger(final ExtendedLogger logger, final String name) {
        this.log4jLogger = logger;
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public void trace(final String message) {
        this.log4jLogger.logIfEnabled(FQCN, Level.TRACE, null, message);
    }

    public void trace(final String message, final Object o) {
        this.log4jLogger.logIfEnabled(FQCN, Level.TRACE, null, message, o);
    }

    public void trace(final String message, final Object param1, final Object param2) {
        this.log4jLogger.logIfEnabled(FQCN, Level.TRACE, null, message, param1, param2);
    }

    public void trace(final String message, final Object... params) {
        this.log4jLogger.logIfEnabled(FQCN, Level.TRACE, null, message, params);
    }

    public void trace(final String message, final Throwable t) {
        this.log4jLogger.logIfEnabled(FQCN, Level.TRACE, null, message, t);
    }

    public void trace(final Marker marker, final String s) {
        this.log4jLogger.logIfEnabled(FQCN, TRACE, getMarker(marker), s);
    }

    public void trace(final Marker marker, final String s, final Object o) {
        this.log4jLogger.logIfEnabled(FQCN, TRACE, getMarker(marker), s, o);
    }

    public void trace(final Marker marker, final String s, final Object o, final Object o1) {
        this.log4jLogger.logIfEnabled(FQCN, TRACE, getMarker(marker), s, o, o1);
    }

    public void trace(final Marker marker, final String s, final Object... objects) {
        this.log4jLogger.logIfEnabled(FQCN, TRACE, getMarker(marker), s, objects);
    }

    public void trace(final Marker marker, final String s, final Throwable throwable) {
        this.log4jLogger.logIfEnabled(FQCN, TRACE, getMarker(marker), s, throwable);
    }


    public void debug(final String message) {
        this.log4jLogger.logIfEnabled(FQCN, DEBUG, null, message);
    }

    public void debug(final String message, final Object o) {
        this.log4jLogger.logIfEnabled(FQCN, DEBUG, null, message, o);
    }

    public void debug(final String message, final Object arg1, final Object arg2) {
        this.log4jLogger.logIfEnabled(FQCN, DEBUG, null, message, arg1, arg2);
    }

    public void debug(final String message, final Object... params) {
        this.log4jLogger.logIfEnabled(FQCN, DEBUG, null, message, params);
    }

    public void debug(final String message, final Throwable t) {
        this.log4jLogger.logIfEnabled(FQCN, DEBUG, null, message, t);
    }

    public void debug(final Marker marker, final String message) {
        this.log4jLogger.logIfEnabled(FQCN, DEBUG, getMarker(marker), message);
    }

    public void debug(final Marker marker, final String message, final Object o) {
        this.log4jLogger.logIfEnabled(FQCN, DEBUG, getMarker(marker), message, o);
    }

    public void debug(final Marker marker, final String message, final Object o, final Object o1) {
        this.log4jLogger.logIfEnabled(FQCN, DEBUG, getMarker(marker), message, o, o1);
    }

    public void debug(final Marker marker, final String message, final Object... params) {
        this.log4jLogger.logIfEnabled(FQCN, DEBUG, getMarker(marker), message, params);
    }

    public void debug(final Marker marker, final String message, final Throwable throwable) {
        this.log4jLogger.logIfEnabled(FQCN, DEBUG, getMarker(marker), message, throwable);
    }


    public void info(final String message) {
        this.log4jLogger.logIfEnabled(FQCN, INFO, null, message);
    }

    public void info(final String message, final Object o) {
        this.log4jLogger.logIfEnabled(FQCN, INFO, null, message, o);
    }

    public void info(final String message, final Object arg1, final Object arg2) {
        this.log4jLogger.logIfEnabled(FQCN, INFO, null, message, arg1, arg2);
    }

    public void info(final String message, final Object... params) {
        this.log4jLogger.logIfEnabled(FQCN, INFO, null, message, params);
    }

    public void info(final String message, final Throwable t) {
        this.log4jLogger.logIfEnabled(FQCN, INFO, null, message, t);
    }

    public void info(final Marker marker, final String message) {
        this.log4jLogger.logIfEnabled(FQCN, INFO, getMarker(marker), message);
    }

    public void info(final Marker marker, final String message, final Object o) {
        this.log4jLogger.logIfEnabled(FQCN, INFO, getMarker(marker), message, o);
    }

    public void info(final Marker marker, final String message, final Object o, final Object o1) {
        this.log4jLogger.logIfEnabled(FQCN, INFO, getMarker(marker), message, o, o1);
    }

    public void info(final Marker marker, final String message, final Object... params) {
        this.log4jLogger.logIfEnabled(FQCN, INFO, getMarker(marker), message, params);
    }

    public void info(final Marker marker, final String message, final Throwable throwable) {
        this.log4jLogger.logIfEnabled(FQCN, INFO, getMarker(marker), message, throwable);
    }

    public void warn(final String message) {
        this.log4jLogger.logIfEnabled(FQCN, WARN, null, message);
    }

    public void warn(final String message, final Object o) {
        this.log4jLogger.logIfEnabled(FQCN, WARN, null, message, o);
    }

    public void warn(final String message, final Object arg1, final Object arg2) {
        this.log4jLogger.logIfEnabled(FQCN, WARN, null, message, arg1, arg2);
    }

    public void warn(final String message, final Object... args) {
        this.log4jLogger.logIfEnabled(FQCN, WARN, null, message, args);
    }

    public void warn(final String message, final Throwable t) {
        this.log4jLogger.logIfEnabled(FQCN, WARN, null, message, t);
    }

    public void warn(final Marker marker, final String message) {
        this.log4jLogger.logIfEnabled(FQCN, WARN, getMarker(marker), message);
    }

    public void warn(final Marker marker, final String message, final Object o) {
        this.log4jLogger.logIfEnabled(FQCN, WARN, getMarker(marker), message, o);
    }

    public void warn(final Marker marker, final String message, final Object o, final Object o1) {
        this.log4jLogger.logIfEnabled(FQCN, WARN, getMarker(marker), message, o, o1);
    }

    public void warn(final Marker marker, final String message, final Object... params) {
        this.log4jLogger.logIfEnabled(FQCN, WARN, getMarker(marker), message, params);
    }

    public void warn(final Marker marker, final String message, final Throwable throwable) {
        this.log4jLogger.logIfEnabled(FQCN, WARN, getMarker(marker), message, throwable);
    }

    public void error(final String errorId) {
        final LoggingContext loggingContext = new LoggingContext()
                .with(ERROR_ID, errorId);

        runWithContext(
                () -> this.log4jLogger.logIfEnabled(FQCN, ERROR, null, "Error {} occurred", errorId),
                loggingContext
        );

    }

    public void error(final String errorId, final Object message) {
        final LoggingContext loggingContext = new LoggingContext()
                .with(ERROR_ID, errorId);

        runWithContext(
                () -> this.log4jLogger.logIfEnabled(FQCN, ERROR, null, message.toString()),
                loggingContext
        );
    }

    public void error(final String errorId, final Object message, final Object param) {
        final LoggingContext loggingContext = new LoggingContext()
                .with(ERROR_ID, errorId);

        runWithContext(
                () -> this.log4jLogger.logIfEnabled(FQCN, ERROR, null, message.toString(), param),
                loggingContext
        );
    }

    public void error(final String errorId, final Object... messageAndParams) {
        final LoggingContext loggingContext = new LoggingContext()
                .with(ERROR_ID, errorId);

        runWithContext(
                () -> this.log4jLogger.logIfEnabled(FQCN, ERROR, null,
                        messageAndParams[0].toString(), copyOfRange(messageAndParams, 1, messageAndParams.length)),
                loggingContext
        );
    }

    public void error(final String errorId, final Throwable throwable) {
        final LoggingContext loggingContext = new LoggingContext()
                .with(ERROR_ID, errorId);

        runWithContext(
                () -> this.log4jLogger.logIfEnabled(FQCN, ERROR, null, throwable.getMessage(), throwable),
                loggingContext
        );
    }


    public void error(final Marker marker, final String errorId) {
        final LoggingContext loggingContext = new LoggingContext()
                .with(ERROR_ID, errorId);

        runWithContext(
                () -> this.log4jLogger.logIfEnabled(FQCN, ERROR, getMarker(marker), "Error {} occurred", errorId),
                loggingContext
        );
    }

    public void error(final Marker marker, final String errorId, final Object message) {
        final LoggingContext loggingContext = new LoggingContext()
                .with(ERROR_ID, errorId);

        runWithContext(
                () -> this.log4jLogger.logIfEnabled(FQCN, ERROR, getMarker(marker), message.toString()),
                loggingContext
        );
    }

    public void error(final Marker marker, final String errorId, final Object message, final Object param) {
        final LoggingContext loggingContext = new LoggingContext()
                .with(ERROR_ID, errorId);

        runWithContext(
                () -> this.log4jLogger.logIfEnabled(FQCN, ERROR, getMarker(marker), message.toString(), param),
                loggingContext
        );
    }

    public void error(final Marker marker, final String errorId, final Object... messageAndParams) {
        final LoggingContext loggingContext = new LoggingContext()
                .with(ERROR_ID, errorId);

        runWithContext(
                () -> this.log4jLogger.logIfEnabled(FQCN, ERROR, getMarker(marker),
                        messageAndParams[0].toString(), copyOfRange(messageAndParams, 1, messageAndParams.length)),
                loggingContext
        );
    }

    public void error(final Marker marker, final String errorId, final Throwable throwable) {
        final LoggingContext loggingContext = new LoggingContext()
                .with(ERROR_ID, errorId);

        runWithContext(
                () -> this.log4jLogger.logIfEnabled(FQCN, ERROR, getMarker(marker), throwable.getMessage(), throwable),
                loggingContext
        );
    }

    private static org.apache.logging.log4j.Marker getMarker(final Marker marker) {
        if (marker == null) {
            return null;
        }
        if (marker instanceof Log4jMarker) {
            return ((Log4jMarker) marker).getLog4jMarker();
        }

        Log4jMarkerFactory factory = (Log4jMarkerFactory) SLF4JServiceProviderImpl.getSingleton().getMarkerFactory();
        return ((Log4jMarker) factory.getMarker(marker)).getLog4jMarker();

    }

    public boolean isTraceEnabled() {
        return this.log4jLogger.isEnabled(Level.TRACE, null, null);
    }

    public boolean isTraceEnabled(final Marker marker) {
        return this.log4jLogger.isEnabled(Level.TRACE, getMarker(marker), null);
    }

    public boolean isWarnEnabled() {
        return this.log4jLogger.isEnabled(WARN, null, null);
    }

    public boolean isWarnEnabled(final Marker marker) {
        return this.log4jLogger.isEnabled(WARN, getMarker(marker), null);
    }

    public boolean isDebugEnabled() {
        return this.log4jLogger.isEnabled(DEBUG, null, null);
    }

    public boolean isDebugEnabled(final Marker marker) {
        return this.log4jLogger.isEnabled(DEBUG, getMarker(marker), null);
    }

    public boolean isInfoEnabled() {
        return this.log4jLogger.isEnabled(INFO, null, null);
    }

    public boolean isInfoEnabled(final Marker marker) {
        return this.log4jLogger.isEnabled(INFO, getMarker(marker), null);
    }

    public boolean isErrorEnabled() {
        return this.log4jLogger.isEnabled(ERROR, null, null);
    }

    public boolean isErrorEnabled(final Marker marker) {
        return this.log4jLogger.isEnabled(ERROR, getMarker(marker), null);
    }

    @Override
    public void log(final org.slf4j.Marker marker, final String fqcn, final int level, final String message, final Object[] params, Throwable throwable) {
        Level log4jLevel = getLevel(level);
        org.apache.logging.log4j.Marker log4jMarker = getMarker(marker);
        if (this.log4jLogger.isEnabled(log4jLevel, log4jMarker, message, params)) {
            Message msg;
            if (params == null) {
                msg = new SimpleMessage(message);
            } else {
                msg = new ParameterizedMessage(message, params, throwable);
                if (throwable != null) {
                    throwable = msg.getThrowable();
                }
            }

            this.log4jLogger.logMessage(fqcn, log4jLevel, log4jMarker, msg, throwable);
        }
    }

    private static Level getLevel(final int i) {
        switch (i) {
            case 0:
                return Level.TRACE;
            case 10:
                return Level.DEBUG;
            case 20:
                return Level.INFO;
            case 30:
                return Level.WARN;
            case 40:
            default:
                return Level.ERROR;
        }
    }

    private static void runWithContext(
            final Runnable callable,
            final LoggingContext loggingContext
    ) {
        try (CloseableThreadContext.Instance ignored = CloseableThreadContext.putAll(loggingContext)) {
            try {
                callable.run();
            } catch (Exception exception) {
                StatusLogger.getLogger().warn("Unexpected exception during logging", exception);
            }
        }
    }
}
