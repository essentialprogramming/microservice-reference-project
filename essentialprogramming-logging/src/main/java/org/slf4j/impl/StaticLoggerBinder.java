package org.slf4j.impl;

import com.logging.Log4jFactory;
import org.slf4j.ILoggerFactory;
import org.slf4j.spi.LoggerFactoryBinder;

/**
 * A custom static logger binder that binds to a user defined {@link ILoggerFactory}.
 */
public class StaticLoggerBinder implements LoggerFactoryBinder {

    private static final String LOGGER_FACTORY_CLASS = Log4jFactory.class.getName();
    private static class LoggerBinderHolder {
        private static final StaticLoggerBinder SLF4J_STATIC_LOGGER_BINDER = new StaticLoggerBinder();
    }

    public static StaticLoggerBinder getSingleton() {
        return LoggerBinderHolder.SLF4J_STATIC_LOGGER_BINDER;
    }

    /**
     * The ILoggerFactory instance returned by the {@link #getLoggerFactory} method should always be
     * the same object.
     */
    private final ILoggerFactory loggerFactory;

    private StaticLoggerBinder() {
        loggerFactory = new Log4jFactory();
    }

    @Override
    public ILoggerFactory getLoggerFactory() {
        return loggerFactory;
    }

    @Override
    public String getLoggerFactoryClassStr() {
        return LOGGER_FACTORY_CLASS;
    }
}
