package com.logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.spi.AbstractLoggerAdapter;
import org.apache.logging.log4j.spi.LoggerContext;
import org.apache.logging.log4j.util.StackLocatorUtil;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

import java.util.function.Predicate;


public class Log4jLoggerFactory extends AbstractLoggerAdapter<Logger>  implements ILoggerFactory {

    private static final Predicate<Class<?>> CALLER_PREDICATE = (clazz) -> !AbstractLoggerAdapter.class.equals(clazz) && !clazz.getName().startsWith("org.slf4j");

    protected Logger newLogger(final String name, final LoggerContext context) {
        final String key = "ROOT".equals(name) ? "" : name;
        return new Log4jLogger(context.getLogger(key), name);
    }

    protected LoggerContext getContext() {
        final Class<?> anchor = LogManager.getFactory().isClassLoaderDependent() ? StackLocatorUtil.getCallerClass(Log4jLoggerFactory.class, CALLER_PREDICATE) : null;
        return anchor == null ? LogManager.getContext(false) : this.getContext(anchor);
    }

}
