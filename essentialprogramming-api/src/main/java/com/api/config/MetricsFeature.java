package com.api.config;

import io.micrometer.core.instrument.MeterRegistry;

import javax.inject.Inject;
import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;

@Provider
public class MetricsFeature implements DynamicFeature {

    @Inject
    private MeterRegistry meterRegistry;

    public void setMeterRegistry(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @Override
    public void configure(final ResourceInfo resourceInfo, final FeatureContext context) {

        if (!resourceInfo.getResourceClass().getName().contains("Controller")) {
            return;
        }
        context.register(new MetricsFilter(resourceInfo, meterRegistry));
    }

}
