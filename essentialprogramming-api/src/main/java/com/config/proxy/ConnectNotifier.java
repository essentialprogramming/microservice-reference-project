package com.config.proxy;

import io.undertow.client.ClientCallback;
import io.undertow.client.ClientConnection;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.ServerConnection;
import io.undertow.server.handlers.proxy.ProxyCallback;
import io.undertow.server.handlers.proxy.ProxyConnection;
import io.undertow.util.HttpString;
import lombok.extern.slf4j.Slf4j;
import org.xnio.IoUtils;

import java.io.IOException;

@Slf4j
public final class ConnectNotifier implements ClientCallback<ClientConnection> {

    private final ProxyCallback<ProxyConnection> callback;
    private final HttpServerExchange exchange;

    public ConnectNotifier(ProxyCallback<ProxyConnection> callback, HttpServerExchange exchange) {
        this.callback = callback;
        this.exchange = exchange;
    }

    @Override
    public void completed(final ClientConnection connection) {
        log.info("Callback completed");

        final ServerConnection serverConnection = exchange.getConnection();
        serverConnection.addCloseListener(serverConnection1 -> IoUtils.safeClose(connection));

        exchange.getResponseHeaders().add(new HttpString("Cache-Control"), "no-cache");
        exchange.getResponseHeaders().add(new HttpString("X-Accel-Buffering"), "no");
        exchange.getResponseHeaders().add(new HttpString("Connection"), "keep-alive");

        callback.completed(exchange, new ProxyConnection(connection, "/"));
    }

    @Override
    public void failed(IOException e) {
        log.info("Callback failed");
        callback.failed(exchange);
    }
}
