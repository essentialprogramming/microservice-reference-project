package com.util.async;

import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ManagedThreadPoolExecutor extends ThreadPoolExecutor {

    private static final Logger log = LoggerFactory.getLogger(ManagedThreadPoolExecutor.class);
    private static final AtomicInteger seq = new AtomicInteger(1);

    public ManagedThreadPoolExecutor(
            final int corePoolSize,
            final int maximumPoolSize,
            final long keepAliveTime,
            final TimeUnit unit,
            final BlockingQueue<Runnable> workQueue,
            final RejectedExecutionHandler handler) {

        super(
                corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                unit,
                workQueue,
                new NamedThreadFactory("micro-service-reference-project-thread-pool"),
                handler
        );
    }

    public void stop() {
        shutdown();
        log.info("ManagedExecutorService - stopping (waiting for all tasks to complete - max 60 seconds)");
        try {
            if (!awaitTermination(60, TimeUnit.SECONDS)) {
                shutdownNow();
            }
            log.info("ManagedExecutorService stopped");
        } catch (InterruptedException e) {
            shutdownNow();
            Thread.currentThread().interrupt();
        }
    }


    private static class NamedThreadFactory implements ThreadFactory {

        private final String poolName;
        private final ThreadFactory threadFactory;

        public NamedThreadFactory(final String poolName) {
            this.poolName = poolName;
            threadFactory = Executors.defaultThreadFactory();
        }

        @Override
        public Thread newThread(@NonNull Runnable runnable) {
            final Thread thread = threadFactory.newThread(runnable);
            final String workerName = "APP-" + seq.getAndIncrement();
            thread.setName(thread.getName()
                    .replace("pool", poolName)
                    .replace("-thread-", "-worker")
            );

            return thread;
        }
    }
}
