package com.config;

import java.time.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClockConfig {

    public static final String UTC_CLOCK = "system.clock.utc";

    @Bean(UTC_CLOCK)
    public Clock clock() {
        return Clock.systemUTC();
    }
}
