package com.util.annotations;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses(
    value = {
        @ApiResponse(responseCode = "400", description = "Bad request, most probably due to malformed request syntax."),
        @ApiResponse(responseCode = "422", description = "Unprocessable Entity, which means that the syntax of the request entity is correct, but server was unable to process it."),
        @ApiResponse(responseCode = "401", description = "Unauthorized, could be missing authorization header."),
        @ApiResponse(responseCode = "403", description = "Forbidden."),
        @ApiResponse(responseCode = "500", description = "Internal server error.")
    }
)
public @interface ApiErrorResponses {
}
