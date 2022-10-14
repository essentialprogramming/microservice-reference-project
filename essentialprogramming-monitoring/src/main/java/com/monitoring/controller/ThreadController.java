package com.monitoring.controller;

import com.monitoring.config.Anonymous;
import com.monitoring.exception.ExceptionHandler;
import com.monitoring.output.ThreadInfoJSON;
import com.monitoring.service.ThreadService;
import com.util.async.Computation;
import com.util.async.ExecutorsProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Response;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutorService;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Tag(name = "Thread Services")
@Path("/thread")
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class ThreadController {

    private final ThreadService threadService;


    @GET
    @Path("/dump")
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    @Operation(summary = "Retrieve a thread dump",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieve a thread dump",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ThreadInfoJSON.class)))
            })
    @Anonymous
    public void getThreadDump(@Suspended AsyncResponse asyncResponse) {

        ExecutorService executorService = ExecutorsProvider.getExecutorService();
        Computation.computeAsync(threadService::createThreadDump, executorService)
                .thenApplyAsync(json -> asyncResponse.resume(Response.ok(json).build()), executorService)
                .exceptionally(error -> asyncResponse.resume(ExceptionHandler.handleException((CompletionException) error)));
    }

}
