package com.logging;

import org.apache.logging.log4j.ThreadContext;
import org.slf4j.spi.MDCAdapter;

import java.util.Map;

public class Log4jMDCAdapter implements MDCAdapter {

    public Log4jMDCAdapter() {
    }

    public void put(final String key, final String val) {
        ThreadContext.put(key, val);
    }

    public String get(final String key) {
        return ThreadContext.get(key);
    }

    public void remove(final String key) {
        ThreadContext.remove(key);
    }

    public void clear() {
        ThreadContext.clearMap();
    }

    public Map<String, String> getCopyOfContextMap() {
        return ThreadContext.getContext();
    }

    @SuppressWarnings("unchecked")
    public void setContextMap(final Map map) {
        ThreadContext.clearMap();
        ThreadContext.putAll(map);
    }
}
