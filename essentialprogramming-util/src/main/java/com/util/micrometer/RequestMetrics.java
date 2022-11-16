package com.util.micrometer;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.container.ResourceInfo;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RequestMetrics {

    private static final Map<String, Counter> counters = new ConcurrentHashMap<>();
    private static final String METRIC_KEY = "counter.request";

    @Inject
    private MeterRegistry meterRegistry;

    public void setMeterRegistry(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    public void addMetric(ResourceInfo resourceInfo) {

        String controllerName = resourceInfo.getResourceClass().getSimpleName();
        String fullMethodName = resourceInfo.getResourceMethod().toGenericString();
        String methodName = resourceInfo.getResourceMethod().getName();
        String endpointPath = resourceInfo.getResourceClass().getAnnotation(Path.class).value()
                + resourceInfo.getResourceMethod().getAnnotation(Path.class).value();

        counters.put(fullMethodName, Counter
                .builder(METRIC_KEY + "." + controllerName + "." + methodName)
                .description("counter of API requests " + getHttpMethod(resourceInfo) + " : " + endpointPath)
                .register(meterRegistry));
    }

    private String getHttpMethod(ResourceInfo resourceInfo) {

        if (resourceInfo.getResourceMethod().isAnnotationPresent(GET.class)) {
            return GET.class.getSimpleName();
        } else if (resourceInfo.getResourceMethod().isAnnotationPresent(POST.class)) {
            return POST.class.getSimpleName();
        } else if (resourceInfo.getResourceMethod().isAnnotationPresent(DELETE.class)) {
            return DELETE.class.getSimpleName();
        } else if (resourceInfo.getResourceMethod().isAnnotationPresent(PUT.class)) {
            return PUT.class.getSimpleName();
        } else if (resourceInfo.getResourceMethod().isAnnotationPresent(PATCH.class)) {
            return PATCH.class.getSimpleName();
        } else if (resourceInfo.getResourceMethod().isAnnotationPresent(HEAD.class)) {
            return HEAD.class.getSimpleName();
        } else if (resourceInfo.getResourceMethod().isAnnotationPresent(OPTIONS.class)) {
            return OPTIONS.class.getSimpleName();
        } else return "";
    }

    public void increment(String methodName) {
        if (counters.containsKey(methodName)) {
            counters.get(methodName).increment();
        }
    }
}
