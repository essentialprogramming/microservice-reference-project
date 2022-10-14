package com.util.micrometer;

import com.util.async.ExecutorsProvider;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;

@Component
public class ExecutorServiceMetrics {

    @Bean
    public ExecutorService monitorExecutorService(final MeterRegistry registry) {
        return io.micrometer.core.instrument.binder.jvm.ExecutorServiceMetrics
                .monitor(registry, ExecutorsProvider.getManagedExecutorService(), "monitored.executorservice");
    }
}
