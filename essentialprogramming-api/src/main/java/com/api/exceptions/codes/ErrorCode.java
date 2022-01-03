package com.api.exceptions.codes;


import com.util.exceptions.ErrorCodes;

/**
 * Error code enum for authentication process.
 *
 */
public enum ErrorCode implements ErrorCodes.ErrorCode {


    USER_NOT_FOUND(1, "User not found"),;

    static {
        ErrorCodes.registerErrorCodes(ErrorCode.class);
    }

    private final long code;
    private final String description;

    ErrorCode(long code, String description) {
        this.code = code;
        this.description = description;
    }

    public long getCode() {
        return this.code;
    }

    public String getDescription() {
        return description;
    }

}
