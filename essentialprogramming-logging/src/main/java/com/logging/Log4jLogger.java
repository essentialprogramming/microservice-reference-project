package com.logging;

import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.spi.ExtendedLogger;
import org.slf4j.Marker;
import org.slf4j.impl.StaticMarkerBinder;

import static com.logging.LoggingKey.*;
import static org.apache.logging.log4j.Level.*;

public class Log4jLogger implements org.slf4j.Logger {

    public static final String FQCN = Log4jLogger.class.getName();
    private final ExtendedLogger logger;
    private final String name;

    public Log4jLogger(final ExtendedLogger logger, final String name) {
        this.logger = logger;
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public void trace(final String format) {
        this.logger.logIfEnabled(FQCN, TRACE, null, format);
    }

    public void trace(final String format, final Object o) {
        this.logger.logIfEnabled(FQCN, TRACE, null, format, o);
    }

    public void trace(final String format, final Object arg1, final Object arg2) {
        this.logger.logIfEnabled(FQCN, TRACE, null, format, arg1, arg2);
    }

    public void trace(final String format, final Object... args) {
        this.logger.logIfEnabled(FQCN, TRACE, null, format, args);
    }

    public void trace(final String format, final Throwable t) {
        this.logger.logIfEnabled(FQCN, TRACE, null, format, t);
    }

    public boolean isTraceEnabled() {
        return this.logger.isEnabled(TRACE, null, null);
    }

    public boolean isTraceEnabled(final Marker marker) {
        return this.logger.isEnabled(TRACE, getMarker(marker), null);
    }

    public void trace(final Marker marker, final String s) {
        this.logger.logIfEnabled(FQCN, TRACE, getMarker(marker), s);
    }

    public void trace(final Marker marker, final String s, final Object o) {
        this.logger.logIfEnabled(FQCN, TRACE, getMarker(marker), s, o);
    }

    public void trace(final Marker marker, final String s, final Object o, final Object o1) {
        this.logger.logIfEnabled(FQCN, TRACE, getMarker(marker), s, o, o1);
    }

    public void trace(final Marker marker, final String s, final Object... objects) {
        this.logger.logIfEnabled(FQCN, TRACE, getMarker(marker), s, objects);
    }

    public void trace(final Marker marker, final String s, final Throwable throwable) {
        this.logger.logIfEnabled(FQCN, TRACE, getMarker(marker), s, throwable);
    }

    public void debug(final String format) {
        this.logger.logIfEnabled(FQCN, DEBUG, null, format);
    }

    public void debug(final String format, final Object o) {
        this.logger.logIfEnabled(FQCN, DEBUG, null, format, o);
    }

    public void debug(final String format, final Object arg1, final Object arg2) {
        this.logger.logIfEnabled(FQCN, DEBUG, null, format, arg1, arg2);
    }

    public void debug(final String format, final Object... args) {
        this.logger.logIfEnabled(FQCN, DEBUG, null, format, args);
    }

    public void debug(final String format, final Throwable t) {
        this.logger.logIfEnabled(FQCN, DEBUG, null, format, t);
    }

    public boolean isDebugEnabled() {
        return this.logger.isEnabled(DEBUG, null, null);
    }

    public boolean isDebugEnabled(final Marker marker) {
        return this.logger.isEnabled(DEBUG, getMarker(marker), null);
    }

    public void debug(final Marker marker, final String s) {
        this.logger.logIfEnabled(FQCN, DEBUG, getMarker(marker), s);
    }

    public void debug(final Marker marker, final String s, final Object o) {
        this.logger.logIfEnabled(FQCN, DEBUG, getMarker(marker), s, o);
    }

    public void debug(final Marker marker, final String s, final Object o, final Object o1) {
        this.logger.logIfEnabled(FQCN, DEBUG, getMarker(marker), s, o, o1);
    }

    public void debug(final Marker marker, final String s, final Object... objects) {
        this.logger.logIfEnabled(FQCN, DEBUG, getMarker(marker), s, objects);
    }

    public void debug(final Marker marker, final String s, final Throwable throwable) {
        this.logger.logIfEnabled(FQCN, DEBUG, getMarker(marker), s, throwable);
    }

    public void info(final String format) {
        this.logger.logIfEnabled(FQCN, INFO, null, format);
    }

    public void info(final String format, final Object o) {
        this.logger.logIfEnabled(FQCN, INFO, null, format, o);
    }

    public void info(final String format, final Object arg1, final Object arg2) {
        this.logger.logIfEnabled(FQCN, INFO, null, format, arg1, arg2);
    }

    public void info(final String format, final Object... args) {
        this.logger.logIfEnabled(FQCN, INFO, null, format, args);
    }

    public void info(final String format, final Throwable t) {
        this.logger.logIfEnabled(FQCN, INFO, null, format, t);
    }

    public boolean isInfoEnabled() {
        return this.logger.isEnabled(INFO, null, null);
    }

    public boolean isInfoEnabled(final Marker marker) {
        return this.logger.isEnabled(INFO, getMarker(marker), null);
    }

    public void info(final Marker marker, final String s) {
        this.logger.logIfEnabled(FQCN, INFO, getMarker(marker), s);
    }

    public void info(final Marker marker, final String s, final Object o) {
        this.logger.logIfEnabled(FQCN, INFO, getMarker(marker), s, o);
    }

    public void info(final Marker marker, final String s, final Object o, final Object o1) {
        this.logger.logIfEnabled(FQCN, INFO, getMarker(marker), s, o, o1);
    }

    public void info(final Marker marker, final String s, final Object... objects) {
        this.logger.logIfEnabled(FQCN, INFO, getMarker(marker), s, objects);
    }

    public void info(final Marker marker, final String s, final Throwable throwable) {
        this.logger.logIfEnabled(FQCN, INFO, getMarker(marker), s, throwable);
    }

    public void warn(final String format) {
        this.logger.logIfEnabled(FQCN, WARN, null, format);
    }

    public void warn(final String format, final Object o) {
        ThreadContext.put(WARN_ID.getValue(), format);
        if (o instanceof String) this.logger.logIfEnabled(FQCN, WARN, null, (String)o);
        else this.logger.logIfEnabled(FQCN, WARN, null, format, o);
        ThreadContext.clearMap();
    }

    public void warn(final String format, final Object arg1, final Object arg2) {
        this.logger.logIfEnabled(FQCN, WARN, null, format, arg1, arg2);
    }

    public void warn(final String format, final Object... args) {
        this.logger.logIfEnabled(FQCN, WARN, null, format, args);
    }

    public void warn(final String format, final Throwable t) {
        this.logger.logIfEnabled(FQCN, WARN, null, format, t);
    }

    public boolean isWarnEnabled() {
        return this.logger.isEnabled(WARN, null, null);
    }

    public boolean isWarnEnabled(final Marker marker) {
        return this.logger.isEnabled(WARN, getMarker(marker), null);
    }

    public void warn(final Marker marker, final String s) {
        this.logger.logIfEnabled(FQCN, WARN, getMarker(marker), s);
    }

    public void warn(final Marker marker, final String s, final Object o) {
        this.logger.logIfEnabled(FQCN, WARN, getMarker(marker), s, o);
    }

    public void warn(final Marker marker, final String s, final Object o, final Object o1) {
        this.logger.logIfEnabled(FQCN, WARN, getMarker(marker), s, o, o1);
    }

    public void warn(final Marker marker, final String s, final Object... objects) {
        this.logger.logIfEnabled(FQCN, WARN, getMarker(marker), s, objects);
    }

    public void warn(final Marker marker, final String s, final Throwable throwable) {
        this.logger.logIfEnabled(FQCN, WARN, getMarker(marker), s, throwable);
    }

    public void error(final String format) {
        this.logger.logIfEnabled(FQCN, ERROR, null, format);
    }

    public void error(final String format, final Object o) {
        ThreadContext.put(ERROR_ID.getValue(), format);
        if (o instanceof String) this.logger.logIfEnabled(FQCN, ERROR, null, (String)o);
        else this.logger.logIfEnabled(FQCN, ERROR, null, format, o);
        ThreadContext.clearMap();
    }

    public void error(final String format, final Object arg1, final Object arg2) {
        this.logger.logIfEnabled(FQCN, ERROR, null, format, arg1, arg2);
    }

    public void error(final String format, final Object... args) {
        this.logger.logIfEnabled(FQCN, ERROR, null, format, args);
    }

    public void error(final String format, final Throwable t) {
        this.logger.logIfEnabled(FQCN, ERROR, null, format, t);
    }

    public boolean isErrorEnabled() {
        return this.logger.isEnabled(ERROR, null, null);
    }

    public boolean isErrorEnabled(final Marker marker) {
        return this.logger.isEnabled(ERROR, getMarker(marker), null);
    }

    public void error(final Marker marker, final String s) {
        this.logger.logIfEnabled(FQCN, ERROR, getMarker(marker), s);
    }

    public void error(final Marker marker, final String s, final Object o) {
        this.logger.logIfEnabled(FQCN, ERROR, getMarker(marker), s, o);
    }

    public void error(final Marker marker, final String s, final Object o, final Object o1) {
        this.logger.logIfEnabled(FQCN, ERROR, getMarker(marker), s, o, o1);
    }

    public void error(final Marker marker, final String s, final Object... objects) {
        this.logger.logIfEnabled(FQCN, ERROR, getMarker(marker), s, objects);
    }

    public void error(final Marker marker, final String s, final Throwable throwable) {
        this.logger.logIfEnabled(FQCN, ERROR, getMarker(marker), s, throwable);
    }

    private static org.apache.logging.log4j.Marker getMarker(final Marker marker) {
        if (marker == null) {
            return null;
        } else if (marker instanceof Log4jMarker) {
            return ((Log4jMarker)marker).getLog4jMarker();
        } else {
            Log4jMarkerFactory factory = (Log4jMarkerFactory) StaticMarkerBinder.SINGLETON.getMarkerFactory();
            return ((Log4jMarker)factory.getMarker(marker)).getLog4jMarker();
        }
    }
}
