package com.api.controller;

import com.api.config.Anonymous;
import com.api.model.UserInput;
import com.api.output.UserJSON;
import com.api.security.AllowUserIf;
import com.api.service.UserService;
import com.exception.ExceptionHandler;
import com.internationalization.Messages;
import com.token.validation.auth.AuthUtils;
import com.util.annotations.ApiErrorResponses;
import com.util.async.Computation;
import com.util.async.ExecutorsProvider;
import com.util.enums.HTTPCustomStatus;
import com.util.enums.Language;
import com.util.exceptions.ApiException;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutorService;

import static com.api.config.AppConfig.USER_API;
import static com.api.exceptions.codes.ErrorCode.USER_ALREADY_EXISTS;

@Tag(description = USER_API, name = "User Services")
@ApiErrorResponses
@Path("/v1/")
@Slf4j
public class UserController {

    private final UserService userService;
    private final MeterRegistry meterRegistry;

    @Context
    private Language language;

    @Inject
    public UserController(final UserService userService, MeterRegistry meterRegistry) {
        this.userService = userService;
        this.meterRegistry = meterRegistry;
    }


    @POST
    @Path("user")
    @Consumes("application/json")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Create user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully return created user details.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserJSON.class)))
    })
    @Anonymous
    public void createUser(@Valid UserInput userInput, @Suspended AsyncResponse asyncResponse) {

        final ExecutorService executorService = ExecutorsProvider.getExecutorService();
        Computation.computeAsync(() -> createUser(userInput, language), executorService)
                .thenApplyAsync(json -> asyncResponse.resume(Response.status(201).entity(json).build()), executorService)
                .exceptionally(error -> asyncResponse.resume(ExceptionHandler.handleException((CompletionException) error)));

    }

    private Serializable createUser(UserInput userInput, Language language) throws GeneralSecurityException, ApiException {
        boolean isValid = userService.checkAvailabilityByEmail(userInput.getEmail());
        if (!isValid) {

            log.error(USER_ALREADY_EXISTS.getCode(), "User with email=`{}` already exists", userInput.getEmail());
            throw new ApiException(Messages.get("EMAIL.ALREADY.TAKEN", language), HTTPCustomStatus.INVALID_REQUEST);
        }
        return userService.save(userInput, language);

    }


    @GET
    @Path("user")
    @Consumes("application/json")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Load user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully return authenticated user details.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserJSON.class)))
            })
    @RolesAllowed({"visitor", "administrator"})
    @AllowUserIf("hasAnyRole(@privilegeService.getPrivilegeRoles(\"LOAD.USER\")) OR hasAnyAuthority('PERMISSION_read:user', 'PERMISSION_edit:user') AND @userService.checkEmailExists(authentication.getPrincipal())")
    public void load(@HeaderParam("Authorization") String authorization, @Suspended AsyncResponse asyncResponse) {

        final String bearer = AuthUtils.extractBearerToken(authorization);
        final String email = AuthUtils.getClaim(bearer, "email");

        final ExecutorService executorService = ExecutorsProvider.getExecutorService();
        Computation.computeAsync(() -> loadUser(email), executorService)
                .thenApplyAsync(json -> asyncResponse.resume(Response.ok(json).build()), executorService)
                .exceptionally(error -> asyncResponse.resume(ExceptionHandler.handleException((CompletionException) error)));

    }

    private Serializable loadUser(String email) throws ApiException {
        return userService.loadUser(email, language);
    }

    @GET
    @Path("users")
    @Consumes("application/json")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Load all users",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully return list of all users.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserJSON.class)))
            })
    @RolesAllowed({"visitor", "administrator"})
    @AllowUserIf("hasAuthority('PERMISSION_do:anything') OR hasAnyAuthority('PERMISSION_read:user', 'PERMISSION_edit:user') AND @userService.checkEmailExists(authentication.getPrincipal())")
    @Anonymous
    public void loadAll(@HeaderParam("Authorization") String authorization, @Suspended AsyncResponse asyncResponse) {

        final Timer timer = Timer
                .builder("timer.load.users")
                .description("measures the time taken to load all users")
                .register(meterRegistry);
        final Callable<List<UserJSON>> findAllUsers = timer
                .wrap((Callable<List<UserJSON>>) this::findAllUsers);

        final ExecutorService executorService = ExecutorsProvider.getExecutorService();
        Computation.computeAsync(findAllUsers, executorService)
                .thenApplyAsync(json -> asyncResponse.resume(Response.ok(json).build()), executorService)
                .exceptionally(error -> asyncResponse.resume(ExceptionHandler.handleException((CompletionException) error)));
    }

    public List<UserJSON> findAllUsers() {
        return userService.loadAllAsJSON();
    }

    @DELETE
    @Path("/user")
    @Consumes("application/json")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Delete User",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Returns a custom JSON with OK status and a message," +
                            " if the user with the given email has been successfully deleted.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"status\": \"ok\", " +
                                            "\"message\": \"User was successfully deleted!\"}"))),
                    @ApiResponse(responseCode = "404", description = "User not found."),
            })
    @AllowUserIf("hasAnyRole(@privilegeService.getPrivilegeRoles(\"LOAD.USER\")) OR hasAnyAuthority('PERMISSION_read:user', 'PERMISSION_edit:user') AND @userService.checkEmailExists(authentication.getPrincipal())")
    public void deleteUser(@HeaderParam("Authorization") String authorization,

                           @Parameter(description = "Email",  required = true, example = "razvanpaulp@gmail.com")
                           @QueryParam("email") @Valid @NotNull(message = "Email must be provided!") final String userEmail,

                           @Suspended AsyncResponse asyncResponse) {

        ExecutorService executorService = ExecutorsProvider.getExecutorService();
        Computation.computeAsync(() -> deleteUser(userEmail), executorService)
                .thenApplyAsync(json -> asyncResponse.resume(Response.ok(json).build()), executorService)
                .exceptionally(error -> asyncResponse.resume(ExceptionHandler.handleException((CompletionException) error)));

    }

    private Serializable deleteUser(String userEmail) throws ApiException {
        return userService.deleteUser(userEmail);
    }

}