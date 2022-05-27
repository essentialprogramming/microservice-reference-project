package com.util.function;

/**
 * A {@link java.util.function.Function} which may throw a checked exception
 *
 * @param <T> the type of the input to the function
 * @param <R> the type of the result of the function
 * @param <E> the type of exception thrown by the function
 */
@FunctionalInterface
public interface CheckedFunction<T, R, E extends Throwable> {
    R apply(T t) throws E;
}