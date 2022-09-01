package com.util.logging;

import com.util.logging.wrapper.MetricLogger;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

public class LogFactory implements ILoggerFactory {

    @Override
    public Logger getLogger(String s) {
        return new MetricLogger(s);
    }
}
