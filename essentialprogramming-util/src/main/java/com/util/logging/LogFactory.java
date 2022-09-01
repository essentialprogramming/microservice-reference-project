package com.util.logging;

import com.util.logging.wrapper.Log;
import com.util.logging.wrapper.MetricLogger;

public class LogFactory {

    public static Log getLogger(final Class<?> clazz) {
        return new MetricLogger(clazz);
    }
}
