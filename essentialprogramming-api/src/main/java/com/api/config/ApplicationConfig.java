package com.api.config;


import com.api.controller.*;
import com.exception.BeanValidationExceptionHandler;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.util.enums.Language;
import io.swagger.v3.jaxrs2.integration.JaxrsApplicationScanner;
import io.swagger.v3.jaxrs2.integration.resources.AcceptHeaderOpenApiResource;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.process.internal.RequestScoped;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;
import java.util.Collections;

/**
 * JAX-RS application configuration class.
 */

@ApplicationPath("/api")
public class ApplicationConfig extends ResourceConfig {

    public ApplicationConfig() {

        //make sure this is performed before the registration of the Jackson stuff
        register(BeanValidationExceptionHandler.class, 1);

        //CORS
        register(CorsFilter.class);

        //JSON Conversions
        register(JacksonJaxbJsonProvider.class);

        //Needed for upload
        register(MultiPartFeature.class);

        //Validate JWT Token
        register(SecurityFeature.class);

        //Initialize controller counters
        register(RequestMetricsFeature.class);

        register(UserController.class);
        register(WelcomeController.class);
        register(PDFController.class);


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
                        .title(AppConfig.APP_TITLE)
                        .description(
                                "Essential Programming endpoints using OpenAPI 3.0")
                        .version("v1")
                )
                .schemaRequirement("Bearer", new SecurityScheme()
                        .name("Authorization")
                        .description("JWT Authorization header using the Bearer scheme. Example: \\\\\\\"Authorization: Bearer {token}\\\\\\\"")
                        .type(SecurityScheme.Type.APIKEY)
                        .in(SecurityScheme.In.HEADER)
                )
                .security(Collections.singletonList(new SecurityRequirement().addList("Bearer")));

        //openAPI.tags(openAPI.getTags().stream().sorted(Comparator.comparing(Tag::getName)).collect(Collectors.toList()));


        final SwaggerConfiguration oasConfig = new SwaggerConfiguration()
                .readAllResources(false)
                .scannerClass(JaxrsApplicationScanner.class.getName())
                .openAPI(openAPI)
                .prettyPrint(true);

        final AcceptHeaderOpenApiResource openApiResource = new AcceptHeaderOpenApiResource();
        openApiResource.setOpenApiConfiguration(oasConfig);

        // register resources ✅
        register(openApiResource);
    }

}
