package com.authentication.channel;

/**
 * Represents the different access channels a user may log in by.
 */
public enum AccessChannel {
    AUTHORIZATION_CODE, PASSWORD, REFRESH_TOKEN, OTP, UNDEFINED;

    public static AccessChannel getAccessChannel(String param) {

        switch (param) {
            case "code":
                return AccessChannel.AUTHORIZATION_CODE;
            case "password":
                return AccessChannel.PASSWORD;
            case "refresh":
                return AccessChannel.REFRESH_TOKEN;
            case "otp":
                return AccessChannel.OTP;
            default:
                return AccessChannel.UNDEFINED;
        }

    }
}