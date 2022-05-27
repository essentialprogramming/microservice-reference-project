package com.util.function;

/**
 * A {@link java.util.function.Consumer} which may throw a checked exception
 *
 * @param <T> the type of the input to the operation
 * @param <E> the type of exception thrown by the consumer
 */
@FunctionalInterface
public interface CheckedConsumer<T, E extends Throwable> {
    void accept(T t) throws E;
}