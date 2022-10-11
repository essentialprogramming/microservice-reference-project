package com.logging;

import org.slf4j.ILoggerFactory;
import org.slf4j.IMarkerFactory;
import org.slf4j.spi.MDCAdapter;
import org.slf4j.spi.SLF4JServiceProvider;

/**
 * A custom logger binder that binds to a user defined {@link ILoggerFactory}.
 */
public class SLF4JServiceProviderImpl implements SLF4JServiceProvider {

    private static final String API_VERSION = "2.0.0";
    private static class LoggerBinderHolder {
        private static final SLF4JServiceProviderImpl SLF4J_SERVICE_PROVIDER = new SLF4JServiceProviderImpl();
    }

    public static SLF4JServiceProviderImpl getSingleton() {
        return LoggerBinderHolder.SLF4J_SERVICE_PROVIDER;
    }

    /**
     * The ILoggerFactory, IMarkerFactory and MDCAdapter instances should always be
     * the same object.
     */
    private final ILoggerFactory loggerFactory;
    private final IMarkerFactory markerFactory;
    private final MDCAdapter mdcAdapter;

    public SLF4JServiceProviderImpl() {
        loggerFactory = new Log4jLoggerFactory();
        markerFactory = new Log4jMarkerFactory();
        mdcAdapter= new Log4jMDCAdapter();
    }

    @Override
    public ILoggerFactory getLoggerFactory() {
        return loggerFactory;
    }

    @Override
    public IMarkerFactory getMarkerFactory() {
        return markerFactory;
    }

    @Override
    public MDCAdapter getMDCAdapter() {
        return mdcAdapter;
    }

    @Override
    public String getRequestedApiVersion() {
        return API_VERSION;
    }

    @Override
    public void initialize() {
     //Currently, not needed.
    }
}
