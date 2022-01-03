package com.api.controller;

import com.util.web.SessionUtils;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.inject.Inject;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet("quiz/*")
public class DemoQuizServlet extends HttpServlet {

    @Inject
    private ServletContext context;


    @Override
    public void init(final ServletConfig config) throws ServletException {
        super.init(config);
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if (SessionUtils.getAttribute(request,"email") != null) {
            context.getRequestDispatcher("/app/demo.html").forward(request, response);
        } else {
            response.sendRedirect("/auth/sign-in?redirect_uri=http://localhost:8080");
        }


    }
}
