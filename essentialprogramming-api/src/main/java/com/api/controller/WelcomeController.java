package com.api.controller;

import com.api.config.Anonymous;
import com.util.async.ExecutorsProvider;
import com.exception.ExceptionHandler;
import com.util.async.Computation;
import com.util.enums.Language;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutorService;

@Tag(description = "Test API", name = "Test purpose only")
@Path("/")
public class WelcomeController {

    private static final Logger LOG = LoggerFactory.getLogger(WelcomeController.class);

    @Context
    private Language language;

    @Context
    ServletContext servletContext;

    public WelcomeController() {
        LOG.info("Starting..");
    }


    //........................................................................................................................
    //Test purpose only
    @GET
    @Path("test/{name}")
    @Consumes("application/json")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Test purpose only")
    @Tag(description = "Test API", name = "Test purpose only")
    @Anonymous
    public void test(@HeaderParam("Accept-Language") String lang, @PathParam("name") String name, @Suspended AsyncResponse asyncResponse) {
        ExecutorService executorService = ExecutorsProvider.getExecutorService();
        Computation.computeAsync(this::test, executorService)
                .thenApplyAsync(json -> asyncResponse.resume(Response.ok(json).build()), executorService)
                .exceptionally(error -> asyncResponse.resume(ExceptionHandler.handleException((CompletionException) error)));

    }

    private Language test() throws IOException {
        return language;
    }

    @GET
    @Path("/questions")
    @Produces(MediaType.APPLICATION_JSON)
    @Anonymous
    public List<QuestionJSON> getQuestionsByQuiz() {

        QuestionJSON question = QuestionJSON.builder()
                .question("Why are you here ?")
                .answers(Arrays.asList("Don't know", "Looking around", "I want to build an awesome app"))
                .correctAnswer("c")
                .build();

        QuestionJSON question2 = QuestionJSON.builder()
                .question("What's next ?")
                .answers(Arrays.asList("Leave this page", "Get a coffee", "Build an awesome app"))
                .correctAnswer("c")
                .build();


        return Arrays.asList(question, question2);

    }

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuestionJSON {

        private int id;
        private String question;
        private String correctAnswer;
        private String quiz;
        private List<String> answers;
    }
}
