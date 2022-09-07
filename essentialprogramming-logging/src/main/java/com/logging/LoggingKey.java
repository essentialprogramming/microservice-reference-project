package com.logging;

import lombok.Getter;

public enum LoggingKey {

    ERROR_ID("errorID"),
    INFO_ID("infoID"),
    WARN_ID("warnID");

    @Getter
    private final String value;

    LoggingKey(final String value) {
        this.value = value;
    }
}
