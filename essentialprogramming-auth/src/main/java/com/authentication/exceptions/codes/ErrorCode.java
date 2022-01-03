package com.authentication.exceptions.codes;


import com.util.exceptions.ErrorCodes;

/**
 * Error code enum for authentication process.
 *
 */
public enum ErrorCode implements ErrorCodes.ErrorCode {


    PRIVATE_KEY_IS_NULL(1, "Private Key must not be null"),
    UNABLE_TO_SIGN_TOKEN(2, "Token can not be signed"),
    UNABLE_TO_ENCRYPT_TOKEN(3, "Token can not be encrypted"),
    UNABLE_TO_GET_PUBLIC_KEY(4, "Unable to obtain Public Key: "),
    UNABLE_TO_GET_PRIVATE_KEY(5, "Unable to obtain Private Key: "),
    KEY_ENTRY_DOES_NOT_EXIST(6, "There is no keystore entry with the given alias. Alias name to be checked: "),
    ALGORITHM_NOT_AVAILABLE(7, "Requested cryptographic algorithm is not available in the environment."),
    UNDEFINED_ACCESS_CHANNEL(8, "Undefined access channel"),
    SYMMETRIC_KEY_IS_NULL(9, "Symmetric Key must not be null"),


    PASSWORD_HASH_CREATION_NOT_SUCCESSFUL(40, "Password Hash creation not successful"),
    REFRESH_TOKEN_NOT_ALLOWED(50, "Implicit flow must not issue a refresh token"),
    UNABLE_TO_AUTHENTICATE(60, "Authentication failed");




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
