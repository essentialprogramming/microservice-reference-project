package com.exceptions.codes;

import com.util.exceptions.ErrorCodes;

public enum ErrorCode implements ErrorCodes.ErrorCode {

    UNABLE_TO_DECRYPT_PASSWORD("JOB1000", "Unable to decrypt jobrunr dashboard password");

    private final String code;
    private final String description;

    /**
     * The standard constructor.
     *
     * @param code        the error code
     * @param description the error description
     */
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
