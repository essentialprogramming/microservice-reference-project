package com.util.micrometer;

import com.util.async.ExecutorsProvider;
import com.util.async.ManagedThreadPoolExecutor;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class ThreadPoolMetrics {

    private static final String THREAD_POOL_METRIC = "gauge.threadpool.";

    private static final String CORE_POOL_SIZE = "core.pool.size";
    private static final String POOL_SIZE = "pool.size";
    private static final String LARGEST_POOL_SIZE = "largest.pool.size";
    private static final String MAX_POOL_SIZE = "max.pool.size";
    private static final String TASK_COUNT = "task.count";
    private static final String ACTIVE_COUNT = "active.count";
    private static final String COMPLETED_TASKS = "completed.tasks.count";
    private static final String QUEUE_SIZE = "queue.size";

    public ThreadPoolMetrics(MeterRegistry meterRegistry) {
        ManagedThreadPoolExecutor managedThreadPoolExecutor = ExecutorsProvider.getManagedExecutorService();
        registerGauges(meterRegistry, managedThreadPoolExecutor);
    }

    private void registerGauges(MeterRegistry meterRegistry, ManagedThreadPoolExecutor managedThreadPoolExecutor) {

        Gauge.builder(THREAD_POOL_METRIC + CORE_POOL_SIZE, managedThreadPoolExecutor, ManagedThreadPoolExecutor::getCorePoolSize)
                .description("core number of threads")
                .register(meterRegistry);
        Gauge.builder(THREAD_POOL_METRIC + POOL_SIZE, managedThreadPoolExecutor, ManagedThreadPoolExecutor::getPoolSize)
                .description("current number of threads in the pool")
                .register(meterRegistry);
        Gauge.builder(THREAD_POOL_METRIC + LARGEST_POOL_SIZE, managedThreadPoolExecutor, ManagedThreadPoolExecutor::getLargestPoolSize)
                .description("largest number of threads that have ever simultaneously been in the pool")
                .register(meterRegistry);
        Gauge.builder(THREAD_POOL_METRIC + MAX_POOL_SIZE, managedThreadPoolExecutor, ManagedThreadPoolExecutor::getMaximumPoolSize)
                .description("maximum allowed number of threads")
                .register(meterRegistry);
        Gauge.builder(THREAD_POOL_METRIC + TASK_COUNT, managedThreadPoolExecutor, ManagedThreadPoolExecutor::getTaskCount)
                .description("approximate total number of tasks that have ever been scheduled for execution")
                .register(meterRegistry);
        Gauge.builder(THREAD_POOL_METRIC + ACTIVE_COUNT, managedThreadPoolExecutor, ManagedThreadPoolExecutor::getActiveCount)
                .description("number of threads that are actively executing tasks")
                .register(meterRegistry);
        Gauge.builder(THREAD_POOL_METRIC + COMPLETED_TASKS, managedThreadPoolExecutor, ManagedThreadPoolExecutor::getCompletedTaskCount)
                .description("total number of tasks that have completed execution")
                .register(meterRegistry);
        Gauge.builder(THREAD_POOL_METRIC + QUEUE_SIZE, managedThreadPoolExecutor, value -> managedThreadPoolExecutor.getQueue().size())
                .description("total number of queued tasks")
                .register(meterRegistry);
    }
}
