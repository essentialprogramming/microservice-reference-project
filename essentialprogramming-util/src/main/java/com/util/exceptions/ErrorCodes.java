package com.util.exceptions;


import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public class ErrorCodes {

    public interface ErrorCode {

        /**
         * Get the error code.
         */
        String getCode();

        /**
         * Get the error description.
         */
        String getDescription();

    }

    private static final Map<String, ErrorCode> errors = new HashMap<>();


    /**
     * Private constructor to hide the default public one.
     */
    private ErrorCodes() {
        // Private constructor to hide the default public one.
    }

    public static <E extends Enum<E>> void registerErrorCodes(Class<E> clazz) {
        EnumSet.allOf(clazz).forEach(error -> errors.put(((ErrorCode) error).getCode(), (ErrorCode) error));
    }

    public static String getMessage(String key) {
        return errors.get(key).getDescription();
    }

    public static Map<String, ErrorCode> getErrors() {
        return errors;
    }
}
