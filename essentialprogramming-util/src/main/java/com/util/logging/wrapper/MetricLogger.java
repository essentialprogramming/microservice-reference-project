package com.util.logging.wrapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.ThreadContext;
import org.slf4j.Marker;

import static com.util.logging.LoggingKey.*;

public class MetricLogger implements org.slf4j.Logger {

    private final Logger log;

    public MetricLogger(final String name) {
        log = LogManager.getLogger(name);
    }

    @Override
    public String getName() {
        return log.getName();
    }

    @Override
    public boolean isTraceEnabled() {
        return log.isTraceEnabled();
    }

    @Override
    public void trace(String s) {
        log.trace(s);
    }

    @Override
    public void trace(String s, Object o) {
        log.trace(s, o);
    }

    @Override
    public void trace(String s, Object o, Object o1) {
        log.trace(s, o, o1);
    }

    @Override
    public void trace(String s, Object... objects) {
        log.trace(s, objects);
    }

    @Override
    public void trace(String s, Throwable throwable) {
        log.trace(s, throwable);
    }

    @Override
    public boolean isTraceEnabled(Marker marker) {
        return log.isTraceEnabled(MarkerManager.getMarker(marker.getName()));
    }

    @Override
    public void trace(Marker marker, String s) {
        log.trace(MarkerManager.getMarker(marker.getName()), s);
    }

    @Override
    public void trace(Marker marker, String s, Object o) {
        log.trace(MarkerManager.getMarker(marker.getName()), s, o);
    }

    @Override
    public void trace(Marker marker, String s, Object o, Object o1) {
        log.trace(MarkerManager.getMarker(marker.getName()), s, o, o1);
    }

    @Override
    public void trace(Marker marker, String s, Object... objects) {
        log.trace(MarkerManager.getMarker(marker.getName()), s, objects);
    }

    @Override
    public void trace(Marker marker, String s, Throwable throwable) {
        log.trace(MarkerManager.getMarker(marker.getName()), s, throwable);
    }

    @Override
    public boolean isDebugEnabled() {
        return log.isDebugEnabled();
    }

    @Override
    public void debug(String s) {
        log.debug(s);
    }

    @Override
    public void debug(String s, Object o) {
        log.debug(s, o);
    }

    @Override
    public void debug(String s, Object o, Object o1) {
        log.debug(s, o, o1);
    }

    @Override
    public void debug(String s, Object... objects) {
        log.debug(s, objects);
    }

    @Override
    public void debug(String s, Throwable throwable) {
        log.debug(s, throwable);
    }

    @Override
    public boolean isDebugEnabled(Marker marker) {
        return log.isDebugEnabled(MarkerManager.getMarker(marker.getName()));
    }

    @Override
    public void debug(Marker marker, String s) {
        log.debug(MarkerManager.getMarker(marker.getName()), s);
    }

    @Override
    public void debug(Marker marker, String s, Object o) {
        log.debug(MarkerManager.getMarker(marker.getName()), s, o);
    }

    @Override
    public void debug(Marker marker, String s, Object o, Object o1) {
        log.debug(MarkerManager.getMarker(marker.getName()), s, o, o1);
    }

    @Override
    public void debug(Marker marker, String s, Object... objects) {
        log.debug(MarkerManager.getMarker(marker.getName()), s, objects);
    }

    @Override
    public void debug(Marker marker, String s, Throwable throwable) {
        log.debug(MarkerManager.getMarker(marker.getName()), s, throwable);
    }

    @Override
    public boolean isInfoEnabled() {
        return log.isInfoEnabled();
    }

    @Override
    public void info(String s) {
        log.info(s);
    }

    @Override
    public void info(String s, Object o) {
        log.info(s, o);
    }

    @Override
    public void info(String s, Object o, Object o1) {
        log.info(s, o, o1);
    }

    @Override
    public void info(String s, Object... objects) {
        log.info(s, objects);
    }

    @Override
    public void info(String s, Throwable throwable) {
        log.info(s, throwable);
    }

    @Override
    public boolean isInfoEnabled(Marker marker) {
        return log.isInfoEnabled(MarkerManager.getMarker(marker.getName()));
    }

    @Override
    public void info(Marker marker, String s) {
        log.info(MarkerManager.getMarker(marker.getName()), s);
    }

    @Override
    public void info(Marker marker, String s, Object o) {
        log.info(MarkerManager.getMarker(marker.getName()), s, o);
    }

    @Override
    public void info(Marker marker, String s, Object o, Object o1) {
        log.info(MarkerManager.getMarker(marker.getName()), s, o, o1);
    }

    @Override
    public void info(Marker marker, String s, Object... objects) {
        log.info(MarkerManager.getMarker(marker.getName()), s, objects);
    }

    @Override
    public void info(Marker marker, String s, Throwable throwable) {
        log.info(MarkerManager.getMarker(marker.getName()), s, throwable);
    }

    @Override
    public boolean isWarnEnabled() {
        return log.isWarnEnabled();
    }

    @Override
    public void warn(String s) {
        log.warn(s);
    }

    @Override
    public void warn(String s, Object o) {
        log.warn(s, o);
    }

    @Override
    public void warn(String s, Object... objects) {
        log.warn(s, objects);
    }

    @Override
    public void warn(String s, Object o, Object o1) {
        log.warn(s, o, o1);
    }

    @Override
    public void warn(String s, Throwable throwable) {
        log.warn(s, throwable);
    }

    @Override
    public boolean isWarnEnabled(Marker marker) {
        return log.isWarnEnabled(MarkerManager.getMarker(marker.getName()));
    }

    @Override
    public void warn(Marker marker, String s) {
        log.warn(MarkerManager.getMarker(marker.getName()), s);
    }

    @Override
    public void warn(Marker marker, String s, Object o) {
        log.warn(MarkerManager.getMarker(marker.getName()), s, o);
    }

    @Override
    public void warn(Marker marker, String s, Object o, Object o1) {
        log.warn(MarkerManager.getMarker(marker.getName()), s, o, o1);
    }

    @Override
    public void warn(Marker marker, String s, Object... objects) {
        log.warn(MarkerManager.getMarker(marker.getName()), s, objects);
    }

    @Override
    public void warn(Marker marker, String s, Throwable throwable) {
        log.warn(MarkerManager.getMarker(marker.getName()), s, throwable);
    }

    @Override
    public boolean isErrorEnabled() {
        return log.isErrorEnabled();
    }

    @Override
    public void error(String s) {
        log.error(s);
    }

    @Override
    public void error(String s, Object o) {
        log.error(s, o);
    }

    @Override
    public void error(String s, Object o, Object o1) {
        log.error(s, o, o1);
    }

    @Override
    public void error(String s, Object... objects) {
        log.error(s, objects);
    }

    @Override
    public void error(String s, Throwable throwable) {
        log.error(s, throwable);
    }

    @Override
    public boolean isErrorEnabled(Marker marker) {
        return log.isErrorEnabled(MarkerManager.getMarker(marker.getName()));
    }

    @Override
    public void error(Marker marker, String s) {
        ThreadContext.put(ERROR_ID.getValue(), marker.getName());
        log.error(MarkerManager.getMarker(marker.getName()), s);
        ThreadContext.clearMap();
    }

    @Override
    public void error(Marker marker, String s, Object o) {
        log.error(MarkerManager.getMarker(marker.getName()), s, o);
    }

    @Override
    public void error(Marker marker, String s, Object o, Object o1) {
        log.error(MarkerManager.getMarker(marker.getName()), s, o, o1);
    }

    @Override
    public void error(Marker marker, String s, Object... objects) {
        log.error(MarkerManager.getMarker(marker.getName()), s, objects);
    }

    @Override
    public void error(Marker marker, String s, Throwable throwable) {
        log.error(MarkerManager.getMarker(marker.getName()), s, throwable);
    }
}
