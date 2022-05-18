package com.server;

import javax.servlet.ServletException;

import com.api.config.SplashMessage;
import com.undertow.standalone.UndertowServer;

import static com.util.cloud.Environment.getProperty;
import static com.config.Log4JConfig.configureLog4j;


public class Server {

    public static void main(String[] args)
            throws ServletException {

        SplashMessage.printSplash();

        configureLog4j();

        final String  host = getProperty("undertow.host", "0.0.0.0");
        final Integer port = getProperty("undertow.port", 8080);

        final UndertowServer server = new UndertowServer(host, port, "essentialProgramming.jar");
        server.start();

    }
}
