package com.exceptions.codes;

import com.util.exceptions.ErrorCodes;

public enum ErrorCode implements ErrorCodes.ErrorCode {

    UNABLE_TO_DECRYPT_PASSWORD("1", "Unable to decrypt jobrunr dashboard password");

    static {
        ErrorCodes.registerErrorCodes(ErrorCode.class);
    }
    private final String code;
    private final String description;

    ErrorCode(String code, String description) {
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
