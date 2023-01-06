package com.monitoring.config;

import com.monitoring.controller.ThreadController;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.monitoring.exception.BeanValidationExceptionHandler;
import io.swagger.v3.jaxrs2.integration.JaxrsApplicationScanner;
import io.swagger.v3.jaxrs2.integration.resources.AcceptHeaderOpenApiResource;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.web.filter.CorsFilter;

import javax.ws.rs.ApplicationPath;

/**
 * JAX-RS application configuration class.
 */

@ApplicationPath("/v1/api/monitoring")
public class ApplicationConfig extends ResourceConfig {

    public ApplicationConfig() {

        //make sure this is performed before the registration of the Jackson stuff
        register(BeanValidationExceptionHandler.class, 1);

        //CORS
        register(CorsFilter.class);

        //Controllers
        register(ThreadController.class);

        //JSON Conversions
        register(JacksonJaxbJsonProvider.class);

        OpenAPI openAPI = new OpenAPI()
                .components(new Components())
                .info(new Info()
                        .title(AppConfig.MONITORING_API)
                        .description(
                                "Monitoring API using OpenAPI 3.0")
                        .version("v1")
                );

        SwaggerConfiguration oasConfig = new SwaggerConfiguration()
                .readAllResources(false)
                .scannerClass(JaxrsApplicationScanner.class.getName())
                .openAPI(openAPI)
                .prettyPrint(true);

        AcceptHeaderOpenApiResource openApiResource = new AcceptHeaderOpenApiResource();
        openApiResource.setOpenApiConfiguration(oasConfig);

        // register resources ✅
        register(openApiResource);
    }

}
