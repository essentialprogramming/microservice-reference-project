package com.util.micrometer;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

@Component
public class ThreadingMetrics {

    private static final String THREAD_COUNT_METRIC = "gauge.threading.";

    private static final String THREAD_COUNT = "threadcount";
    private static final String DAEMON_THREAD_COUNT = "daemonthreadcount";
    private static final String PEAK_THREAD_COUNT = "peakthreadcount";
    private static final String TOTAL_STARTED_THREAD_COUNT = "totalstartedthreadcount";
    private static final String DEADLOCKED_THREAD_COUNT = "deadlockedthreads";

    public ThreadingMetrics(MeterRegistry meterRegistry) {
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        registerGauges(meterRegistry, threadMXBean);
    }

    private void registerGauges(MeterRegistry meterRegistry, ThreadMXBean threadMXBean) {

        Gauge.builder(THREAD_COUNT_METRIC + THREAD_COUNT, threadMXBean, ThreadMXBean::getThreadCount)
                .description("current number of live threads")
                .register(meterRegistry);
        Gauge.builder(THREAD_COUNT_METRIC + DAEMON_THREAD_COUNT, threadMXBean, ThreadMXBean::getDaemonThreadCount)
                .description("current number of live daemon threads")
                .register(meterRegistry);
        Gauge.builder(THREAD_COUNT_METRIC + PEAK_THREAD_COUNT, threadMXBean, ThreadMXBean::getPeakThreadCount)
                .description("peak number of live threads")
                .register(meterRegistry);
        Gauge.builder(THREAD_COUNT_METRIC + TOTAL_STARTED_THREAD_COUNT, threadMXBean, ThreadMXBean::getTotalStartedThreadCount)
                .description("total number of started threads")
                .register(meterRegistry);

        long[] deadlockedThreads = threadMXBean.findDeadlockedThreads();
        Gauge.builder(THREAD_COUNT_METRIC + DEADLOCKED_THREAD_COUNT, threadMXBean,
                        deadlockedThreads != null ? value -> deadlockedThreads.length : value -> 0.0D)
                .description("current number of deadlocked threads")
                .register(meterRegistry);
    }
}
