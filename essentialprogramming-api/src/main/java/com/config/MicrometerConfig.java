package com.config;

import com.api.env.resources.AppResources;
import com.api.exceptions.codes.ErrorCode;
import com.crypto.Crypt;
import com.util.exceptions.ServiceException;
import io.micrometer.core.aop.TimedAspect;
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

import java.security.GeneralSecurityException;
import java.time.Duration;

@Configuration
public class MicrometerConfig {
    private static final String DECRYPTION_KEY = "supercalifragilisticexpialidocious";

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

    @Bean
    public TimedAspect timedAspect(MeterRegistry registry) {
        return new TimedAspect(registry);
    }

    private DatadogConfig configureDatadog() {
        final String apiKey = decryptKey(AppResources.DATADOG_API_KEY);
        final String applicationKey = decryptKey(AppResources.DATADOG_APPLICATION_KEY);

        return new DatadogConfig() {
            @Override
            public String get(String key) {
                return null;
            }

            @Override
            public String apiKey() {
                return apiKey;
            }

            @Override
            public String applicationKey() {
                return applicationKey;
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

    private static String decryptKey(AppResources appResources) {
        try {
            return Crypt.decrypt(appResources.value(), DECRYPTION_KEY);
        } catch (GeneralSecurityException e) {

            if (appResources.equals(AppResources.DATADOG_API_KEY)) {
                throw new ServiceException(ErrorCode.UNABLE_TO_DECRYPT_API_KEY, e);
            } else throw new ServiceException(ErrorCode.UNABLE_TO_DECRYPT_APPLICATION_KEY, e);
        }
    }
}
