package com.config;

import com.api.env.resources.AppResources;
import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import io.micrometer.datadog.DatadogConfig;
import io.micrometer.datadog.DatadogMeterRegistry;
import io.micrometer.jmx.JmxConfig;
import io.micrometer.jmx.JmxMeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class MicrometerConfig {

    @Bean(name = "microMeterRegistry")
    public MeterRegistry meterRegistry() {

        final CompositeMeterRegistry compositeRegistry = new CompositeMeterRegistry();
        final SimpleMeterRegistry oneSimpleMeter = new SimpleMeterRegistry();
        final MeterRegistry jmxRegistry = new JmxMeterRegistry(JmxConfig.DEFAULT, Clock.SYSTEM);
        final DatadogMeterRegistry datadogMeterRegistry = new DatadogMeterRegistry(configureDatadog(), Clock.SYSTEM);

        compositeRegistry.add(oneSimpleMeter);
        compositeRegistry.add(jmxRegistry);
        compositeRegistry.add(datadogMeterRegistry);

        return compositeRegistry;
    }

    private DatadogConfig configureDatadog() {

        return new DatadogConfig() {
            @Override
            public String get(String key) {
                return null;
            }

            @Override
            public String apiKey() {
                return AppResources.DATADOG_API_KEY.value();
            }

            @Override
            public String applicationKey() {
                return AppResources.DATADOG_APPLICATION_KEY.value();
            }

            @Override
            public String uri() {
                return AppResources.DATADOG_URI.value();
            }

            @Override
            public boolean enabled() {
                return AppResources.DATADOG_ENABLED.value();
            }

            @Override
            public Duration step() {
                return Duration.ofSeconds(AppResources.DATADOG_STEP_SECONDS.value());
            }

            @Override
            public boolean descriptions() {
                return AppResources.DATADOG_DESCRIPTION.value();
            }
        };
    }
}
