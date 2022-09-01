package com.util.logging.wrapper;

import com.util.exceptions.ErrorCodes;

public interface Log {

    void error(final ErrorCodes.ErrorCode error);
    void info(final String message);
    void warn(final String message);
}
