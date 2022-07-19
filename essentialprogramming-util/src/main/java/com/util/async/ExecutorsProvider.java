package com.util.async;


import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.util.cloud.Environment.getProperty;

/**
 * ExecutorsProvider class provides static access to application shared ExecutorServices to be used by asynchronous
 * methods (tasks implemented using CompletableFutures that run asynchronously).
 */
public class ExecutorsProvider {

    private static final int SUBMITTED_TASKS_QUEUE_SIZE = getProperty("queue.size", 100);

    private static class ExecutorsServiceHolder {
        static final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);
        static final ManagedThreadPoolExecutor managedExecutorService = getManagedAsyncExecutor();
    }


    public static ExecutorService getExecutorService() {
        return ExecutorsServiceHolder.executorService;
    }

    public static ManagedThreadPoolExecutor getManagedExecutorService() {
        return ExecutorsServiceHolder.managedExecutorService;
    }


    private static ManagedThreadPoolExecutor getManagedAsyncExecutor() {
        int corePoolSize = Runtime.getRuntime().availableProcessors();
        int maxPoolSize = corePoolSize * 2;

        final RejectedExecutionHandler handler = new ThreadPoolExecutor.AbortPolicy();
        //final BlockingQueue<Runnable> queue = new PriorityBlockingQueue<>(SUBMITTED_TASKS_QUEUE_SIZE);
        final BlockingQueue<Runnable> queue = new LinkedBlockingDeque<>(SUBMITTED_TASKS_QUEUE_SIZE);

        final ManagedThreadPoolExecutor executor = new ManagedThreadPoolExecutor(
                corePoolSize, maxPoolSize,
                0L, TimeUnit.MILLISECONDS,
                queue,
                handler
        );

        // Let's start all core threads initially
        executor.prestartAllCoreThreads();
        return executor;
    }
}


