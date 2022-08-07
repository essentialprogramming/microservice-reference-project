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

    public ManagedThreadPoolExecutor(
            final int corePoolSize,
            final int maximumPoolSize,
            final long keepAliveTime,
            final TimeUnit unit,
            final String threadPoolName,
            final BlockingQueue<Runnable> workQueue,
            final RejectedExecutionHandler handler) {

        super(
                corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                unit,
                workQueue,
                new NamedThreadFactory(threadPoolName),
                handler
        );
    }

    public void stop() {
        shutdown(); // Disable new tasks from being submitted
        log.info("ManagedExecutorService - stopping (waiting for all tasks to complete - max 180 seconds)");
        try {
            // Wait a while for existing tasks to terminate
            if (!awaitTermination(180, TimeUnit.SECONDS)) {
                shutdownNow();// Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled
                if (!awaitTermination(180, TimeUnit.SECONDS))
                    log.error("Pool did not terminate");
            }
            log.info("ManagedExecutorService stopped");
        } catch (InterruptedException e) {
            // (Re-)Cancel if current thread also interrupted
            shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();
        }
    }


    private static class NamedThreadFactory implements ThreadFactory {
        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private static final AtomicInteger threadCount = new AtomicInteger(1);
        private final String namePrefix;
        private final ThreadFactory threadFactory;

        public NamedThreadFactory(final String poolName) {
            this.namePrefix = poolName + poolNumber.getAndIncrement() + "-thread-";
            this.threadFactory = Executors.defaultThreadFactory();
        }

        @Override
        public Thread newThread(@NonNull Runnable runnable) {
            final Thread thread = threadFactory.newThread(runnable);
            final String workerName = this.namePrefix + threadCount.getAndIncrement();

            thread.setName(workerName);
            return thread;
        }
    }
}
