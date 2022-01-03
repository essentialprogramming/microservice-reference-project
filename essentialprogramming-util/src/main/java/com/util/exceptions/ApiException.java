package com.util.exceptions;

import com.util.enums.HTTPCustomStatus;


public class ApiException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private final HTTPCustomStatus httpCode;


	public ApiException(String message, HTTPCustomStatus statusCode) {
		super(message);
		this.httpCode = statusCode;
	}


	public ApiException(String message, HTTPCustomStatus statusCode, Throwable cause) {
		super(message, cause);
		this.httpCode = statusCode;
	}

	public HTTPCustomStatus getHttpCode() {
		return httpCode;
	}

}
