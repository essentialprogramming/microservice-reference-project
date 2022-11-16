package com.util.cloud;

import java.util.Optional;
import java.util.function.Function;

/**
 * Fetches a property from the Environment, System Properties, then uses a
 * fallback. Useful in Cloud Native (Docker / Kubernetes) deployments.
 */
public interface Environment {

    Function<String, String> propertyLoader = (key) -> System.getenv().getOrDefault(key, System.getProperty(key));

    /**
     * Return the value for the given key converted to the type of the default value.
     * If the value is null, defaultValue is returned.
     */
    @SuppressWarnings("unchecked")
    static <T> T getProperty(String key, T defaultValue) {
        final String type = defaultValue != null
                ? defaultValue.getClass().getSimpleName().toUpperCase()
                : "STRING";

        final String value = propertyLoader.apply(key);
        final T returnValue = (T) Types.valueOf(type).getValue(value);
        return Optional
                .ofNullable(returnValue)
                .orElse(defaultValue);
    }


    enum Types {
        STRING {
            public String getValue(String value) {
                return value;
            }
        },
        INTEGER {
            public Integer getValue(String value) {
                return value != null ? Integer.valueOf(value) : null;
            }
        },
        LONG {
            public Long getValue(String value) {
                return value != null ? Long.valueOf(value) : null;
            }
        },
        BOOLEAN {
            public Boolean getValue(String value) {
                return value != null ? Boolean.valueOf(value) : null;
            }
        };

        public abstract Object getValue(String key);
    }

}
