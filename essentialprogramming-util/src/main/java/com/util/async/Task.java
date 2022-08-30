package com.util.async;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RequiredArgsConstructor
final class Task<R> implements Runnable, Comparable<Task<R>>, CompletableFuture.AsynchronousCompletionTask {

    private final Callable<R> callable;
    private final CompletableFuture<R> promise;
    private final ExecutionPriority priority;

    @Override
    public void run() {
        final Execution<R> execution = tryExecute(callable);
        if (!execution.wasSuccessful()) {
            promise.completeExceptionally(execution.getFailure());
            return;
        }

        if (!promise.isDone()) promise.complete(execution.getResult());
    }


    private Execution<R> tryExecute(final Callable<R> callable) {
        final Execution<R> execution = Execution.create();
        try {
            R callResult = callable.call();

            execution.setSuccessful(true);
            execution.setResult(callResult);
        } catch (Throwable e) {
            log.warn("Exception occurred in callable: {}", e.getMessage(), e);

            execution.setSuccessful(false);
            execution.setFailure(e);

        }

        return execution;
    }

    public int compareTo(Task<R> task) {
        return getPriority().compareTo(task.getPriority());
    }

    public ExecutionPriority getPriority() {
        return priority;
    }

    public Callable<R> getCallable() {
        return callable;
    }
}
