package com.api.controller;

import com.api.config.Anonymous;
import com.api.entities.User;
import com.api.service.UserService;
import com.api.template.Templates;
import com.template.service.TemplateService;
import com.exception.ExceptionHandler;
import com.util.async.Computation;
import com.util.async.ExecutorsProvider;
import com.util.exceptions.ApiException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.jboss.weld.util.collections.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutorService;

@Tag(description = "Pdf API", name = "Download PDF")
@Path("/")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PDFController {

    private final TemplateService templateService;

    private final UserService userService;

    @POST
    @Path("/pdf")
    @Consumes("application/json")
    @Operation(summary = "Generate PDF",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Demo PDF.",
                            content = @Content(mediaType = "application/octet-stream",
                                    schema = @Schema(implementation = String.class)))
            })
    @Anonymous
    public void generatePDF(@Suspended AsyncResponse asyncResponse) {
        final String mediaType = "application/octet-stream";
        final String fileName = String.format("pdf-example-%s.%s",
                LocalDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                "pdf");

        final ExecutorService executorService = ExecutorsProvider.getExecutorService();
        Computation.computeAsync(this::generatePDF, executorService)
                .thenApplyAsync(fileContent -> asyncResponse.resume(
                        Response.ok(fileContent, mediaType)
                                .header("content-disposition", "attachment; filename=" + fileName + "; filename*=UTF-8''" + fileName)
                                .build()),
                        executorService)
                .exceptionally(error -> asyncResponse.resume(ExceptionHandler.handleException((CompletionException) error)));
    }

    private Serializable generatePDF() throws ApiException {

        final List<User> users = userService.loadAll();

        Map<String, Object> templateVariables = ImmutableMap.<String, Object>builder()
                .put("users", users)
                .build();
        return templateService.generatePDF(Templates.PDF_EXAMPLE, templateVariables);
    }
}
