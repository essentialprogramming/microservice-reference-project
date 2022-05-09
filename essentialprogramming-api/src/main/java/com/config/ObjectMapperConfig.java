package com.config;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObjectMapperConfig {
    private static final String BASE_PACKAGE = "com.config";
    private static final String BEAN_OBJECT_MAPPER = BASE_PACKAGE + ".objectMapper";

    private static final ThreadLocal<ObjectMapper> objectMapper = ThreadLocal
            .withInitial(() -> new ObjectMapper()
                    .setDefaultPropertyInclusion(Include.NON_NULL)
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                    .configure(SerializationFeature.INDENT_OUTPUT, true)
                    .registerModule(new JavaTimeModule()));

    @Bean(BEAN_OBJECT_MAPPER)
    public ObjectMapperProvider objectMapperProvider() {
        return objectMapper::get;
    }

    @FunctionalInterface
    public interface ObjectMapperProvider {
        ObjectMapper getObjectMapper();
    }
}