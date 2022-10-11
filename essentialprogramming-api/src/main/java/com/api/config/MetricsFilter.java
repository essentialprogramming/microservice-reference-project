package com.api.config;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.glassfish.jersey.server.ContainerRequest;

import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MetricsFilter implements ContainerRequestFilter {

    private static final String METRIC_KEY = "counter.request";

    private static final Map<String, Counter> counters = new ConcurrentHashMap<>();


    public MetricsFilter(ResourceInfo resourceInfo, MeterRegistry meterRegistry) {

        String controllerName = resourceInfo.getResourceClass().getSimpleName();
        String fullMethodName = resourceInfo.getResourceMethod().toGenericString();
        String methodName = resourceInfo.getResourceMethod().getName();
        String endpointPath = resourceInfo.getResourceClass().getAnnotation(Path.class).value()
                + resourceInfo.getResourceMethod().getAnnotation(Path.class).value();

        counters.put(fullMethodName, Counter
                .builder(METRIC_KEY + "." + controllerName + "." + methodName)
                .description("count of endpoint calls " + getHttpMethod(resourceInfo) + " : " + endpointPath)
                .register(meterRegistry));
    }

    @Override
    public void filter(final ContainerRequestContext requestContext) {

        String methodName = ((ContainerRequest) requestContext)
                .getUriInfo()
                .getMatchedResourceMethod()
                .getInvocable()
                .getDefinitionMethod()
                .toGenericString();

        if (counters.containsKey(methodName)) {
            counters.get(methodName).increment();
        }
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
}
