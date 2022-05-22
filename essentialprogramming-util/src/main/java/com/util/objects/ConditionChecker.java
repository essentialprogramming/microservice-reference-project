package com.util.objects;

import org.checkerframework.checker.nullness.qual.EnsuresNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Objects;

public final class ConditionChecker {

    private ConditionChecker() {
        // Private constructor to hide the default public one.
    }

    /**
     * Check that an object reference is not null, where a null reference indicates a bug in the code.
     *
     * @param obj        The object reference to check
     * @param objectName The name of the object, to identify it in the exception message
     * @param <T>        The type of object being checked.
     * @return The given object, once it has been guaranteed non-null.
     * @throws IllegalArgumentException when {@code obj} is null
     */
    @EnsuresNonNull("#1")
    public static <T> @NonNull T assertNotNull(final @Nullable T obj, final String objectName) {
        return assertNotNull(obj, objectName, null);
    }

    @EnsuresNonNull("#1")
    public static <T> @NonNull T assertNotNull(final @Nullable T reference, final String objectName, final @Nullable String errorMessage) {
        if (reference == null) {
            Objects.requireNonNull(objectName);
            String message = String.format("%s must not be null", objectName);
            if (errorMessage != null) {
                message = String.format("%s( %s )", message, errorMessage);
            }

            throw new IllegalArgumentException(message);
        }

        return reference;
    }

    /**
     * Used to check a generic condition. Throws a
     * {@link IllegalArgumentException} exception containing the message if the
     * condition returns (@code false). The message is not formatted.
     *
     * @param condition
     *        Result of the condition check.
     * @param message
     *        the unformatted message for the failure.
     *
     * @throws IllegalArgumentException,
     *         if the result is {@code false}.
     */
    public static void check(final boolean condition, final String message) throws IllegalArgumentException {
        if (!condition) {
            throw new IllegalArgumentException(message);
        }
    }
}