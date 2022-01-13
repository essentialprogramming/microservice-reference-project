package com.authentication.exception;

import com.util.exceptions.ApiException;
import com.util.password.PasswordException;
import com.util.web.JsonResponse;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.web.client.HttpClientErrorException;

import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletionException;

public class ExceptionHandler {

    @FunctionalInterface
    interface Strategy<T> {
        Response getResponse(T exception);
    }

    private final static Strategy<ApiException> apiExceptionStrategy = (exception) -> {
        JsonResponse jsonResponse;
        jsonResponse = new JsonResponse()
                .with("message", exception.getMessage())
                .with("code", exception.getHttpCode())
                .done();
        return Response
                .status(exception.getHttpCode().value())
                .entity(jsonResponse)
                .build();
    };

    private final static Strategy<HttpClientErrorException> httpClientErrorException = (exception) -> {
        JsonResponse jsonResponse;
        jsonResponse = new JsonResponse()
                .with("message", exception.getMessage())
                .with("code", exception.getStatusCode().value())
                .done();

        return Response
                .status(exception.getStatusCode().value())
                .entity(jsonResponse)
                .build();
    };

    private final static Strategy<PasswordException> passwordExceptionStrategy = (exception) -> {
        JsonResponse jsonResponse;
        jsonResponse = new JsonResponse()
                .with("message", exception.getMessage())
                .done();
        return Response
                .status(Response.Status.NOT_ACCEPTABLE)
                .entity(jsonResponse)
                .build();
    };

    private final static Strategy<Throwable> defaultStrategy = (exception) -> {
        JsonResponse jsonResponse;
        exception.printStackTrace();
        jsonResponse = new JsonResponse()
                .with("message", "INTERNAL SERVER ERROR")
                .with("code", Response.Status.INTERNAL_SERVER_ERROR)
                .with("exception", ExceptionUtils.getStackTrace(exception))
                .with("rootCause", ExceptionUtils.getRootCauseStackTrace(exception))
                .done();

        return Response
                .serverError()
                .entity(jsonResponse).build();
    };


    private final static Map<Class<? extends Throwable>, Strategy<? extends Throwable>> strategiesMap = new HashMap<>();
    static {
        strategiesMap.put(ApiException.class, apiExceptionStrategy);
        strategiesMap.put(HttpClientErrorException.class, httpClientErrorException);
        strategiesMap.put(PasswordException.class, passwordExceptionStrategy);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static Response handleException(CompletionException completionException) {

        Strategy strategy = strategiesMap.getOrDefault(completionException.getCause().getClass(), defaultStrategy);
        return strategy.getResponse(completionException.getCause());
    }

}
