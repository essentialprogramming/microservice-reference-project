package com.authentication.config;


import com.authentication.controller.AuthenticationController;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.util.enums.Language;
import io.swagger.v3.jaxrs2.integration.resources.AcceptHeaderOpenApiResource;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.process.internal.RequestScoped;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;

/**
 * Declares that this application exposes a REST interface and should have the REST services published under the /api/auth/*
 * URI.
 *
 */
@ApplicationPath("api/auth")
public class ApplicationConfig extends ResourceConfig {

    public ApplicationConfig() {

        register(CorsFilter.class);
        register(AuthenticationController.class);
        register(JacksonJaxbJsonProvider.class);

        register(new AbstractBinder(){
            @Override
            protected void configure() {
                bindFactory(LanguageContextProvider.class)
                        .to(Language.class)
                        .in(RequestScoped.class);
            }
        });

        OpenAPI openAPI = new OpenAPI()
                .components(new Components())
                .info(new Info()
                        .title("Auth API")
                        .description(
                                "Authentication API using OpenAPI 3.0")
                        .version("v1")
                );


        SwaggerConfiguration oasConfig = new SwaggerConfiguration()
                .openAPI(openAPI)
                .prettyPrint(true);

        AcceptHeaderOpenApiResource openApiResource = new AcceptHeaderOpenApiResource();
        openApiResource.setOpenApiConfiguration(oasConfig);
        register(openApiResource);


    }

}
