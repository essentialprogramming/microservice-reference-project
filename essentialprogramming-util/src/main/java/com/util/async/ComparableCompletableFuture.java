package com.util.async;


import java.util.Comparator;
import java.util.concurrent.CompletableFuture;

public class ComparableCompletableFuture<T> extends CompletableFuture<T> implements Comparator<Runnable> {

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public int compare(Runnable o1, Runnable o2) {
        return ((Comparable) o1).compareTo(o2);
    }
}
