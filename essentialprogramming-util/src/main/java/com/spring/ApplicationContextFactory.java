package com.spring;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Map;

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

    public static <T> T getBean(Class<T> type) {
        return ApplicationContextHolder.CONTEXT.getBean(type);
    }

    private static <T> Collection<T> getBeansByTypeAndAnnotation(Class<T> clazz, Class<? extends Annotation> annotationType) {
        Map<String, T> typedBeans = ApplicationContextHolder.CONTEXT.getBeansOfType(clazz);
        Map<String, Object> annotatedBeans = ApplicationContextHolder.CONTEXT.getBeansWithAnnotation(annotationType);
        typedBeans.keySet().retainAll(annotatedBeans.keySet());
        return typedBeans.values();
    }
}
