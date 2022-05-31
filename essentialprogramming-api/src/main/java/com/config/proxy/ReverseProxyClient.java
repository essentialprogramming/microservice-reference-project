package com.config.proxy;


import io.undertow.client.UndertowClient;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.proxy.ProxyCallback;
import io.undertow.server.handlers.proxy.ProxyClient;
import io.undertow.server.handlers.proxy.ProxyConnection;
import lombok.extern.slf4j.Slf4j;
import org.xnio.OptionMap;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ReverseProxyClient implements ProxyClient {

    private static final ProxyTarget TARGET = new ProxyTarget() {};

    private final UndertowClient client;
    private final HttpHandler defaultHttpHandler;
    private final List<String> dashboardPaths = Arrays.asList("/dashboard", "/api/servers", "/api/problems",
            "/api/version", "/api/jobs", "/api/recurring-jobs", "/sse");

    private String dashboardUrl = "http://localhost:1000";


    public ReverseProxyClient(HttpHandler defaultHttpHandler) {
        this.client = UndertowClient.getInstance();
        this.defaultHttpHandler = defaultHttpHandler;
        log.info("Proxy client started...");
    }

    @Override
    public ProxyTarget findTarget(HttpServerExchange exchange) {
        return TARGET;
    }

    @Override
    public void getConnection(ProxyTarget target, HttpServerExchange exchange, ProxyCallback<ProxyConnection> callback, long timeout, TimeUnit timeUnit) {

        if (dashboardPaths.stream().anyMatch(path -> exchange.getRequestPath().startsWith(path))) {
            dashboardUrl += exchange.getRequestURI();
            
            client.connect(
                    new ConnectNotifier(callback, exchange),
                    URI.create(dashboardUrl),
                    exchange.getIoThread(),
                    exchange.getConnection().getByteBufferPool(),
                    OptionMap.EMPTY);
        } else {
            exchange.dispatch(defaultHttpHandler);
        }
    }
}