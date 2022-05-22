package com.api.exceptions.codes;


import com.util.exceptions.ErrorCodes;

/**
 * Error code enum.
 *
 */
public enum ErrorCode implements ErrorCodes.ErrorCode {


    USER_NOT_FOUND("MICRO-SERVICE-ERR1", "User not found"),;

    static {
        ErrorCodes.registerErrorCodes(ErrorCode.class);
    }

    private final String code;
    private final String description;

    ErrorCode(final String code, final String description) {
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
