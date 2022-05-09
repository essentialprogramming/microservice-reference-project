package com.util.objects;

import org.checkerframework.checker.nullness.qual.EnsuresNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Objects;

public final class Preconditions {

    private Preconditions() {
        // Private constructor to hide the default public one.
    }

    /**
     * Check that an object reference is not null, where a null reference indicates a bug in the code.
     *
     * @param obj        The object reference to check
     * @param objectName The name of the object, to identify it in the exception message
     * @param <T>        The type of object being checked.
     * @return The given object, once it has been guaranteed non-null.
     * @throws NullPointerException when {@code obj} is null
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
}