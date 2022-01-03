package com.util.password;


public class PasswordException extends Exception {

    private final PasswordStrength passwordStrength;

    public PasswordException(String message, PasswordStrength passwordStrength) {
        super(message);
        this.passwordStrength = passwordStrength;
    }

    public PasswordStrength getPasswordStrength() {
        return passwordStrength;
    }
}
