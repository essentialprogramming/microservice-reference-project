package com.email.error.codes;

import com.util.exceptions.ErrorCodes;

public enum EmailErrorCode implements ErrorCodes.ErrorCode{

    EMAIL_CLIENT_INITIALIZATION_FAILED("300", "Email client initialization failed"),
    UNABLE_TO_SEND_EMAIL("301", "Unable to send email");


    private final String code;
    private final String description;

    static {
        ErrorCodes.registerErrorCodes(EmailErrorCode.class);
    }

    EmailErrorCode(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return this.code;
    }

    public String getDescription() {
        return description;
    }
}
