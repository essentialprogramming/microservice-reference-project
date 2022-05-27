package com.util.function;

/**
 * A {@link java.util.function.Supplier} that can throw a checked exception.
 *
 * @param <T> the type of object being supplied
 * @param <E> the type of exception that can be thrown
 */
@FunctionalInterface
public interface CheckedSupplier<T, E extends Throwable> {
    T get() throws E;
}
