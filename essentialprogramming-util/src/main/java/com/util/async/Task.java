package com.util.async;


import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;


public class Task<R> implements Runnable, Comparable<Task<R>> {

    private final Callable<R> callable;
    private final ExecutionPriority priority;
    private final CompletableFuture<R> completableFuture;


    public Task(Callable<R> callable, ExecutionPriority priority, CompletableFuture<R> completableFuture) {
        this.callable = callable;
        this.priority = priority;
        this.completableFuture = completableFuture;
    }

    public ExecutionPriority getPriority() {
        return priority;
    }

    public Callable<R> getCallable() {
        return callable;
    }

    @Override
    public int compareTo(Task<R> o) {
        return priority.compareTo(o.getPriority());
    }

    @Override
    public void run() {
        try {
            if(!completableFuture.isDone())
                this.completableFuture.complete(callable.call());
        } catch (Exception e) {
            this.completableFuture.completeExceptionally(e);
        }
    }
}
