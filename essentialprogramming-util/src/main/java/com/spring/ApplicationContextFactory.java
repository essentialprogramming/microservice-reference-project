package com.spring;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

public class ApplicationContextFactory {

    private static final String SPRING_CONFIG_LOCATION = "com";


    private ApplicationContextFactory() {}

    private static class ApplicationContextHolder {
        static final WebApplicationContext CONTEXT = createSpringWebAppContext(SPRING_CONFIG_LOCATION);
    }


    private static AnnotationConfigWebApplicationContext createSpringWebAppContext(String configLocation) {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.setConfigLocation(configLocation);
        return context;
    }

    public static WebApplicationContext getSpringApplicationContext(){
        return ApplicationContextHolder.CONTEXT;
    }

    public static <T> T getBean(String s, Class<T> type) {
        return ApplicationContextHolder.CONTEXT.getBean(s, type);
    }
}
