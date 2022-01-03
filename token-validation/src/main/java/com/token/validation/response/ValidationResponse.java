package com.token.validation.response;

public class ValidationResponse<T> {

    private final boolean isValid;
    private final T result;


    public ValidationResponse(boolean isValid, T claims) {
        this.isValid = isValid;
        this.result = claims;
    }

    public boolean isValid() {
        return isValid;
    }

    public T getClaims() {
        return result;
    }

}
