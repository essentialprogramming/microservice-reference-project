package com.token.validation.jwt.exception;

public class TokenValidationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public TokenValidationException() {}

    public TokenValidationException(String message) {
        super(message);
    }

    public TokenValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
