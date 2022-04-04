package com.authentication.controller;

import com.authentication.channel.AccessChannel;
import com.authentication.exception.ExceptionHandler;
import com.authentication.identityprovider.internal.model.PasswordInput;
import com.authentication.request.AuthRequest;
import com.authentication.response.AccessToken;
import com.authentication.service.AuthenticationService;
import com.util.async.ExecutorsProvider;
import com.util.password.PasswordException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.token.validation.auth.AuthUtils;
import com.util.async.Computation;
import com.util.enums.Language;
import com.util.exceptions.ApiException;
import com.util.web.JsonResponse;
import com.util.web.SessionUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.security.GeneralSecurityException;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutorService;

@Tag(description = "Authorization API", name = "Authorization")
@Path("/")
public class AuthenticationController {

    @Context
    private HttpServletRequest httpRequest;

    @Context
    private Language language;

    private final AuthenticationService authenticationService;

    @Inject
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }


    @POST
    @Path("token")
    @Consumes("application/json")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Authenticate user, return JWT token.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Returns JWT token if user successfully authenticated",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AccessToken.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = JsonResponse.class))),
            })
    public void authenticate(AuthRequest tokenRequest, @Suspended AsyncResponse asyncResponse) {

        ExecutorService executorService = ExecutorsProvider.getExecutorService();
        Computation.computeAsync(() -> authenticate(tokenRequest), executorService)
                .thenApplyAsync(json -> asyncResponse.resume(Response.ok(json).build()), executorService)
                .exceptionally(error -> asyncResponse.resume(ExceptionHandler.handleException((CompletionException) error)));

    }

    private AccessToken authenticate(AuthRequest authRequest) throws  ApiException {
        return authenticationService.authenticate(authRequest, AccessChannel.PASSWORD, language);
    }

    @POST
    @Path("authenticate")
    @Consumes("application/json")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(hidden = true)
    public JsonResponse authenticate(@QueryParam("redirect_uri") String redirectUri, AuthRequest tokenRequest) throws GeneralSecurityException, JsonProcessingException {

        try {
            AccessToken accessToken = authenticate(tokenRequest);
            if (accessToken != null) {
                final String email = AuthUtils.getClaim(accessToken.getAccessToken(), "email");
                SessionUtils.setAttribute(httpRequest, "email", email);
                return new JsonResponse()
                        .with("status", "Redirect")
                        .with("redirectUrl", redirectUri).done();
            } else
                return new JsonResponse()
                        .with("status", "Error")
                        .with("error", "The username or password you entered is incorrect.").done();
        } catch (ApiException exception) {
            return new JsonResponse()
                    .with("status", "Error")
                    .with("error", exception.getMessage()).done();
        }

    }


    @POST
    @Path("password/set")
    @Consumes("application/json")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Set password",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Returns ok if password was set successfully. ",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = JsonResponse.class)))
            })
    public void setPassword(PasswordInput passwordInput, @Suspended AsyncResponse asyncResponse) {

        ExecutorService executorService = ExecutorsProvider.getExecutorService();
        Computation.computeAsync(() -> setPassword(passwordInput), executorService)
                .thenApplyAsync(json -> asyncResponse.resume(Response.ok(json).build()), executorService)
                .exceptionally(error -> asyncResponse.resume(ExceptionHandler.handleException((CompletionException) error)));

    }


    private Serializable setPassword(PasswordInput passwordInput) throws GeneralSecurityException, ApiException, PasswordException {
        return authenticationService.setPassword(passwordInput, language);
    }


    @POST
    @Path("otp")
    @Consumes("application/json")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Authenticate with otp, return JWT token.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Returns JWT token if user successfully authenticated",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AccessToken.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = JsonResponse.class))),
            })
    public void otpLogin(AuthRequest tokenRequest, @Suspended AsyncResponse asyncResponse) {

        ExecutorService executorService = ExecutorsProvider.getExecutorService();
        Computation.computeAsync(() -> otpLogin(tokenRequest), executorService)
                .thenApplyAsync(json -> asyncResponse.resume(Response.ok(json).build()), executorService)
                .exceptionally(error -> asyncResponse.resume(ExceptionHandler.handleException((CompletionException) error)));

    }

    private AccessToken otpLogin(AuthRequest authRequest) throws  ApiException {
        return authenticationService.authenticate(authRequest, AccessChannel.OTP, language);
    }

    @POST
    @Path("otp/generate")
    @Consumes("application/json")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Generate OTP",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Returns 'status ok' if successfully generated",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AccessToken.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = JsonResponse.class))),
            })
    public void generateOtp(@QueryParam("email") String email, @Suspended AsyncResponse asyncResponse) {

        ExecutorService executorService = ExecutorsProvider.getExecutorService();
        Computation.computeAsync(() -> generateOtp(email, language), executorService)
                .thenApplyAsync(json -> asyncResponse.resume(Response.ok(json).build()), executorService)
                .exceptionally(error -> asyncResponse.resume(ExceptionHandler.handleException((CompletionException) error)));

    }

    private Serializable generateOtp(String email, Language language) throws ApiException {
        return authenticationService.generateOtp(email, language);
    }
}
