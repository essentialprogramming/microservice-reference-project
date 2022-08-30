package com.config;

import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import io.micrometer.jmx.JmxConfig;
import io.micrometer.jmx.JmxMeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MicrometerConfig {

    @Bean(name = "microMeterRegistry")
    public MeterRegistry meterRegistry() {

        final CompositeMeterRegistry compositeRegistry = new CompositeMeterRegistry();
        final SimpleMeterRegistry oneSimpleMeter = new SimpleMeterRegistry();
        final MeterRegistry jmxRegistry = new JmxMeterRegistry(JmxConfig.DEFAULT, Clock.SYSTEM);

        compositeRegistry.add(oneSimpleMeter);
        compositeRegistry.add(jmxRegistry);

        return compositeRegistry;
    }
}
