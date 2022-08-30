package com.config;

import com.api.exceptions.codes.ErrorCode;
import com.util.exceptions.ErrorCodes;

public class ErrorConfig {

    public static void registerErrorCodes(){
        ErrorCodes.registerErrorCodes(ErrorCode.class);
        ErrorCodes.registerErrorCodes(com.authentication.exceptions.codes.ErrorCode.class);
        ErrorCodes.registerErrorCodes(com.exceptions.codes.ErrorCode.class);
    }
}
