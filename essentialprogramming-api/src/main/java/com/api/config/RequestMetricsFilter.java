package com.api.config;

import com.util.micrometer.RequestMetrics;
import org.glassfish.jersey.server.ContainerRequest;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;

public class RequestMetricsFilter implements ContainerRequestFilter {

    private final RequestMetrics requestMetrics;

    public RequestMetricsFilter(ResourceInfo resourceInfo, RequestMetrics requestMetrics) {
        this.requestMetrics = requestMetrics;
        requestMetrics.addMetric(resourceInfo);
    }

    @Override
    public void filter(final ContainerRequestContext requestContext) {

        String methodName = ((ContainerRequest) requestContext)
                .getUriInfo()
                .getMatchedResourceMethod()
                .getInvocable()
                .getDefinitionMethod()
                .toGenericString();

        requestMetrics.increment(methodName);
    }
}
