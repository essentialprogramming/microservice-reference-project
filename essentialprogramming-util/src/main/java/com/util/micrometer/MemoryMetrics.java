package com.util.micrometer;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryType;
import java.lang.management.MemoryUsage;

@Component
public class MemoryMetrics {

    private static final String MEMORY_USAGE_METRIC = "gauge.memory.";

    private static final String HEAP_MEMORY_USAGE = "heapmemoryusage";
    private static final String NON_HEAP_MEMORY_USAGE = "nonheapmemoryusage";
    private static final String NON_HEAP = "nonheap";


    public MemoryMetrics(MeterRegistry meterRegistry) {

        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        registerGauges(meterRegistry, memoryMXBean.getHeapMemoryUsage(), HEAP_MEMORY_USAGE);
        registerGauges(meterRegistry, memoryMXBean.getNonHeapMemoryUsage(), NON_HEAP_MEMORY_USAGE);
        ManagementFactory.getMemoryPoolMXBeans()
                .stream()
                .filter(memoryPoolMXBean -> (memoryPoolMXBean.getType() == MemoryType.NON_HEAP))
                .forEach(memoryPoolMXBean -> registerGauges(meterRegistry, memoryPoolMXBean.getUsage(), NON_HEAP + "." + memoryPoolMXBean.getName()));
    }


    private void registerGauges(MeterRegistry meterRegistry, MemoryUsage memoryUsage, String poolName) {
        String escapedPoolName = poolName.toLowerCase().replaceAll(" ", "_");

        Gauge.builder(MEMORY_USAGE_METRIC + escapedPoolName, memoryUsage, MemoryUsage::getCommitted)
                .tag("operation", "committed")
                .description("committed memory size")
                .strongReference(true)
                .register(meterRegistry);
        Gauge.builder(MEMORY_USAGE_METRIC + escapedPoolName, memoryUsage, MemoryUsage::getInit)
                .tag("operation", "init")
                .description("init memory size")
                .strongReference(true)
                .register(meterRegistry);
        Gauge.builder(MEMORY_USAGE_METRIC + escapedPoolName, memoryUsage, MemoryUsage::getMax)
                .tag("operation", "max")
                .description("max memory size")
                .strongReference(true)
                .register(meterRegistry);
        Gauge.builder(MEMORY_USAGE_METRIC + escapedPoolName, memoryUsage, MemoryUsage::getUsed)
                .tag("operation", "used")
                .description("used memory size")
                .strongReference(true)
                .register(meterRegistry);
    }
}
