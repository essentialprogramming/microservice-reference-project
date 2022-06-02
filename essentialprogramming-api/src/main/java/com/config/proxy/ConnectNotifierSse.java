package com.config.proxy;

import io.undertow.client.ClientCallback;
import io.undertow.client.ClientConnection;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.proxy.ProxyCallback;
import io.undertow.server.handlers.proxy.ProxyConnection;
import io.undertow.util.Headers;
import io.undertow.util.HttpString;
import lombok.extern.slf4j.Slf4j;


import java.io.IOException;

@Slf4j
public final class ConnectNotifierSse implements ClientCallback<ClientConnection> {

    private final ProxyCallback<ProxyConnection> callback;
    private final HttpServerExchange exchange;

    public ConnectNotifierSse(final ProxyCallback<ProxyConnection> callback,
                              final HttpServerExchange exchange) {
        this.callback = callback;
        this.exchange = exchange;
    }

    @Override
    public void completed(final ClientConnection connection) {

        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/event-stream; charset=UTF-8");
        exchange.getResponseHeaders().put(Headers.CACHE_CONTROL, "no-cache");
        exchange.getResponseHeaders().put(Headers.CONNECTION, "keep-alive");
        exchange.getResponseHeaders().put(new HttpString("X-Accel-Buffering"), "no");

        callback.completed(exchange, new ProxyConnection(connection, "/"));
    }

    @Override
    public void failed(IOException e) {
        callback.failed(exchange);
    }
}