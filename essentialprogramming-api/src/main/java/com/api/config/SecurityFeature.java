package com.api.config;

import com.authentication.security.KeyStoreService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.Priorities;
import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;

@Provider
public class SecurityFeature implements DynamicFeature {
    @Autowired
    KeyStoreService keyStoreService;

    @Override
    public void configure(final ResourceInfo resourceInfo, final FeatureContext context) {

        if (!resourceInfo.getResourceClass().getName().contains("Controller")) {
            return;
        }

        if (resourceInfo.getResourceMethod().isAnnotationPresent(Anonymous.class)) {
            return;
        }

        context.register(new SecurityFilter(keyStoreService, resourceInfo), Priorities.AUTHENTICATION);

    }
}