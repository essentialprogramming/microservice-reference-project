package com.token.validation.jwt.exception;


public class SignatureVerificationException extends TokenValidationException {

	private static final long serialVersionUID = 1L;

	public SignatureVerificationException() {
	}

	public SignatureVerificationException(String msg) {
		super(msg);
	}

	public SignatureVerificationException(String message, Throwable cause) {
		super(message, cause);
	}



}