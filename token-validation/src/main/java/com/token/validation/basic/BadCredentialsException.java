package com.token.validation.basic;

public class BadCredentialsException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public BadCredentialsException() {
    }

    public BadCredentialsException(String msg) {
        super(msg);
    }

    public BadCredentialsException(String message, Throwable cause) {
        super(message, cause);
    }
}
