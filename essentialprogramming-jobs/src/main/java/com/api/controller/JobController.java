package com.api.controller;

import com.api.enums.CronEnum;
import com.api.service.JobService;
import com.exception.ExceptionHandler;
import com.util.async.Computation;
import com.util.async.ExecutorsProvider;
import com.util.web.JsonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutorService;

@Path("/")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Tag(name = "Job Scheduler Services", description = "Job Scheduler demo with JobRunr")
public class JobController {

    private final JobService jobService;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("enqueue")
    @Operation(summary = "Enqueue job", description = "Enqueue a given story action job",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Returns 200 if job was enequeued successfully")
            }
    )
    public void enqueueJob(@Suspended AsyncResponse asyncResponse) {

        ExecutorService executorService = ExecutorsProvider.getExecutorService();
        Computation.computeAsync(jobService::enqueueJob, executorService)
                .thenApplyAsync(json -> asyncResponse.resume(Response.status(200).entity(json).build()), executorService)
                .exceptionally(error -> asyncResponse.resume(ExceptionHandler.handleException((CompletionException) error)));
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("schedule")
    @Operation(summary = "Schedule job", description = "Schedule a given story action job at a given time",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Returns 200 if job was scheduled successfully")
            }
    )
    public void scheduleJob(@Suspended AsyncResponse asyncResponse, @QueryParam("time") @Schema(example = "16/05/2022 16:00:00") String dateTime) {

        ExecutorService executorService = ExecutorsProvider.getExecutorService();
        Computation.computeAsync(()-> jobService.schedule(dateTime), executorService)
                .thenApplyAsync(json -> asyncResponse.resume(Response.status(200).entity(json).build()), executorService)
                .exceptionally(error -> asyncResponse.resume(ExceptionHandler.handleException((CompletionException) error)));
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("schedule/recurrently")
    @Operation(summary = "Schedule job recurrently", description = "Schedule a given story action job recurrently",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Returns 200 if job was scheduled successfully")
            }
    )
    public void scheduleJobRecurrently(@Suspended AsyncResponse asyncResponse, @QueryParam("cron") @Schema(example = "MINUTELY") CronEnum cron) {

        ExecutorService executorService = ExecutorsProvider.getExecutorService();
        Computation.computeAsync(() -> jobService.scheduleJobRecurrently(cron), executorService)
                .thenApplyAsync(json -> asyncResponse.resume(Response.status(200).entity(json).build()), executorService)
                .exceptionally(error -> asyncResponse.resume(ExceptionHandler.handleException((CompletionException) error)));
    }
}
