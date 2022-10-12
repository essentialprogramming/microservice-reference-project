package com.api.config;

import com.util.micrometer.RequestMetrics;
import io.micrometer.core.instrument.MeterRegistry;

import javax.inject.Inject;
import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;

@Provider
public class RequestMetricsFeature implements DynamicFeature {

    @Inject
    private RequestMetrics requestMetrics;

    public void setRequestMetrics(RequestMetrics requestMetrics) {
        this.requestMetrics = requestMetrics;
    }

    @Override
    public void configure(final ResourceInfo resourceInfo, final FeatureContext context) {

        if (!resourceInfo.getResourceClass().getName().contains("Controller")) {
            return;
        }
        context.register(new RequestMetricsFilter(resourceInfo, requestMetrics));
    }

}
