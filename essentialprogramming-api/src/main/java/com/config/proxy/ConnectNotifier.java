package com.config.proxy;

import io.undertow.client.ClientCallback;
import io.undertow.client.ClientConnection;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.ServerConnection;
import io.undertow.server.handlers.proxy.ProxyCallback;
import io.undertow.server.handlers.proxy.ProxyConnection;
import lombok.extern.slf4j.Slf4j;
import org.xnio.IoUtils;

import java.io.IOException;

@Slf4j
public final class ConnectNotifier implements ClientCallback<ClientConnection> {

    private final ProxyCallback<ProxyConnection> callback;
    private final HttpServerExchange exchange;

    public ConnectNotifier(final ProxyCallback<ProxyConnection> callback,
                           final HttpServerExchange exchange) {
        this.callback = callback;
        this.exchange = exchange;
    }

    @Override
    public void completed(final ClientConnection connection) {

        final ServerConnection serverConnection = exchange.getConnection();
        serverConnection.addCloseListener(___ -> IoUtils.safeClose(connection));

        callback.completed(exchange, new ProxyConnection(connection, "/"));
    }

    @Override
    public void failed(IOException e) {
        callback.failed(exchange);
    }
}
