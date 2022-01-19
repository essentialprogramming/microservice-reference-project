package com.api.controller;

import com.api.config.Anonymous;
import com.api.model.UserInput;
import com.api.output.UserJSON;
import com.api.security.AllowUserIf;
import com.api.service.UserService;
import com.exception.ExceptionHandler;
import com.internationalization.Messages;
import com.token.validation.auth.AuthUtils;
import com.util.async.Computation;
import com.util.async.ExecutorsProvider;
import com.util.enums.HTTPCustomStatus;
import com.util.enums.Language;
import com.util.exceptions.ApiException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.RolesAllowed;
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
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutorService;

@Tag(description = "User API", name = "User Services")
@Path("/v1/")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;


    @Context
    private Language language;


    @POST
    @Path("user/create")
    @Consumes("application/json")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Create user",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Return user if successfully added",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserJSON.class)))
            })
    @Anonymous
    public void createUser(@Valid UserInput userInput, @Suspended AsyncResponse asyncResponse) {

        ExecutorService executorService = ExecutorsProvider.getExecutorService();
        Computation.computeAsync(() -> createUser(userInput, language), executorService)
                .thenApplyAsync(json -> asyncResponse.resume(Response.status(201).entity(json).build()), executorService)
                .exceptionally(error -> asyncResponse.resume(ExceptionHandler.handleException((CompletionException) error)));

    }

    private Serializable createUser(UserInput userInput, Language language) throws GeneralSecurityException, ApiException {
        boolean isValid = userService.checkAvailabilityByEmail(userInput.getEmail());
        if (!isValid) {
            throw new ApiException(Messages.get("EMAIL.ALREADY.TAKEN", language), HTTPCustomStatus.INVALID_REQUEST);
        }
        return userService.save(userInput, language);

    }


    @GET
    @Path("user/load")
    @Consumes("application/json")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Load user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Return authenticated user if it was successfully found",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserJSON.class)))
            })
    @RolesAllowed({"visitor", "administrator"})
    @AllowUserIf("hasAnyRole(@privilegeService.getPrivilegeRoles(\"LOAD.USER\")) OR hasAnyAuthority('PERMISSION_read:user', 'PERMISSION_edit:user') AND @userService.checkEmailExists(authentication.getPrincipal())")
    public void load(@HeaderParam("Authorization") String authorization, @Suspended AsyncResponse asyncResponse) {

        final String bearer = AuthUtils.extractBearerToken(authorization);
        final String email = AuthUtils.getClaim(bearer, "email");

        ExecutorService executorService = ExecutorsProvider.getExecutorService();
        Computation.computeAsync(() -> loadUser(email), executorService)
                .thenApplyAsync(json -> asyncResponse.resume(Response.ok(json).build()), executorService)
                .exceptionally(error -> asyncResponse.resume(ExceptionHandler.handleException((CompletionException) error)));

    }

    private Serializable loadUser(String email) throws ApiException {
        return userService.loadUser(email, language);
    }

    @GET
    @Path("user/all")
    @Consumes("application/json")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Load all users",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Return a list of all users",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserJSON.class)))
            })
    @RolesAllowed({"visitor", "administrator"})
    @AllowUserIf("hasAuthority('PERMISSION_do:anything') OR hasAnyAuthority('PERMISSION_read:user', 'PERMISSION_edit:user') AND @userService.checkEmailExists(authentication.getPrincipal())")
    public void loadAll(@HeaderParam("Authorization") String authorization, @Suspended AsyncResponse asyncResponse) {
        ExecutorService executorService = ExecutorsProvider.getExecutorService();
        Computation.computeAsync(this::findAllUsers, executorService)
                .thenApplyAsync(json -> asyncResponse.resume(Response.ok(json).build()), executorService)
                .exceptionally(error -> asyncResponse.resume(ExceptionHandler.handleException((CompletionException) error)));

    }

    private List<UserJSON> findAllUsers() {
        return userService.loadAll();
    }

    @DELETE
    @Path("/delete")
    @Consumes("application/json")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Delete User",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Returns a custom JSON with OK status and a message," +
                            "if the User with a given email has been deleted.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"status\": \"ok\", " +
                                            "\"message\": \"User was successfully deleted!\"}"))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized."),
                    @ApiResponse(responseCode = "404", description = "User not found!"),
                    @ApiResponse(responseCode = "500", description = "Internal server error.")
            })
    @AllowUserIf("hasAnyRole(@privilegeService.getPrivilegeRoles(\"LOAD.USER\")) OR hasAnyAuthority('PERMISSION_read:user', 'PERMISSION_edit:user') AND @userService.checkEmailExists(authentication.getPrincipal())")
    public void deleteUser(@HeaderParam("Authorization") String authorization,
                           @Valid @NotNull(message = "Email must be provided!")
                           @QueryParam("email") String userEmail,
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