package com.server;

import javax.servlet.ServletException;

import com.undertow.standalone.UndertowServer;

import static com.config.ErrorConfig.registerErrorCodes;
import static com.config.FlywayConfig.migrateDatabase;
import static com.config.SplashMessage.printSplash;
import static com.util.cloud.Environment.getProperty;
import static com.config.Log4JConfig.configureLog4j;


public class Server {

    public static void main(String[] args)
            throws ServletException {

        configureLog4j();
        printSplash();
        migrateDatabase();
        registerErrorCodes();

        final String  host = getProperty("undertow.host", "0.0.0.0");
        final Integer port = getProperty("undertow.port", 8080);

        final UndertowServer server = new UndertowServer(host, port, "essentialProgramming.jar");
        server.start();

    }
}
