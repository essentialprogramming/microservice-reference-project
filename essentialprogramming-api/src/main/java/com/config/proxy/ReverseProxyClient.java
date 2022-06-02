package com.config.proxy;


import io.undertow.client.ClientCallback;
import io.undertow.client.ClientConnection;
import io.undertow.client.UndertowClient;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.proxy.ProxyCallback;
import io.undertow.server.handlers.proxy.ProxyClient;
import io.undertow.server.handlers.proxy.ProxyConnection;
import lombok.extern.slf4j.Slf4j;
import org.xnio.OptionMap;

import java.net.URI;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ReverseProxyClient implements ProxyClient {

    private final String targetUrl;
    private final List<String> apiPaths;
    private final UndertowClient client;
    private final HttpHandler defaultHttpHandler;

    private static final ProxyTarget TARGET = new ProxyTarget() {
    };


    public ReverseProxyClient(final String targetUrl, final List<String> paths, final HttpHandler defaultHttpHandler) {
        log.info("Initializing Proxy client...");

        this.targetUrl = targetUrl;
        this.apiPaths = paths;
        this.defaultHttpHandler = defaultHttpHandler;
        this.client = UndertowClient.getInstance();

    }

    @Override
    public ProxyTarget findTarget(final HttpServerExchange exchange) {
        if (apiPaths.stream().noneMatch(path -> exchange.getRequestPath().startsWith(path))) {
            return null;
        }
        return TARGET;
    }

    @Override
    public void getConnection(final ProxyTarget target,
                              final HttpServerExchange exchange,
                              final ProxyCallback<ProxyConnection> callback,
                              final long timeout,
                              final TimeUnit timeUnit) {

        //No proxy needed ( default case )
        if (apiPaths.stream().noneMatch(path -> exchange.getRequestPath().startsWith(path))) {
            exchange.dispatch(defaultHttpHandler);
        }

        final String targetUri = targetUrl + exchange.getRequestURI();
        final ClientCallback<ClientConnection> clientCallback = exchange.getRequestPath().contains("/sse")
                ? new ConnectNotifierSse(callback, exchange)
                : new ConnectNotifier(callback, exchange);

        exchange.setRelativePath(exchange.getRequestPath()); // need this otherwise proxy forwards to chopped off path
        client.connect(
                clientCallback,
                URI.create(targetUri),
                exchange.getIoThread(),
                exchange.getConnection().getByteBufferPool(),
                OptionMap.EMPTY);
    }
}