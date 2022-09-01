package org.slf4j.impl;

import com.util.logging.LogFactory;
import org.slf4j.ILoggerFactory;
import org.slf4j.spi.LoggerFactoryBinder;

public class StaticLoggerBinder implements LoggerFactoryBinder {

    private static final StaticLoggerBinder SLF4J_STATIC_LOGGER_BINDER = new StaticLoggerBinder();

    public static StaticLoggerBinder getSingleton() {
        return SLF4J_STATIC_LOGGER_BINDER;
    }

    private final ILoggerFactory loggerFactory;

    private StaticLoggerBinder() {
        loggerFactory = new LogFactory();
    }

    @Override
    public ILoggerFactory getLoggerFactory() {
        return loggerFactory;
    }

    @Override
    public String getLoggerFactoryClassStr() {
        return LogFactory.class.getName();
    }
}
