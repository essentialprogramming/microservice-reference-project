package com.template.error.codes;

import com.util.exceptions.ErrorCodes;

public enum TemplateErrorCode implements ErrorCodes.ErrorCode {

    UNABLE_TO_GENERATE_HTML(80, "Unable to generate html template"),
    UNABLE_TO_GENERATE_PDF(90, "Unable to generate PDF"),
    UNABLE_TO_GENERATE_CSV(100, "Unable to generate CSV");

    private final long code;
    private final String description;

    static {
        ErrorCodes.registerErrorCodes(TemplateErrorCode.class);
    }

    TemplateErrorCode(long code, String description) {
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
