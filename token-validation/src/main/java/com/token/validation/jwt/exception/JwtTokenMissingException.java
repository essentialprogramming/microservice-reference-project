package com.token.validation.jwt.exception;

public class JwtTokenMissingException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public JwtTokenMissingException() {
    }

    public JwtTokenMissingException(String msg) {
        super(msg);
    }

    public JwtTokenMissingException(String message, Throwable cause) {
        super(message, cause);
    }
}
