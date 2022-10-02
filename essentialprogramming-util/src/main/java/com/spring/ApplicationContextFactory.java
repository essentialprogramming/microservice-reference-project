package com.spring;

import org.springframework.beans.factory.annotation.BeanFactoryAnnotationUtils;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Map;

import static com.spring.ApplicationContextFactory.ApplicationContextHolder.CONTEXT;

public class ApplicationContextFactory {

    private static final String SPRING_CONFIG_LOCATION = "com";


    private ApplicationContextFactory() {}

    static class ApplicationContextHolder {
        static final WebApplicationContext CONTEXT = createSpringWebAppContext(SPRING_CONFIG_LOCATION);
    }


    private static AnnotationConfigWebApplicationContext createSpringWebAppContext(String configLocation) {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.setConfigLocation(configLocation);

        return context;
    }

    public static WebApplicationContext getSpringApplicationContext(){
        return CONTEXT;
    }

    public static <T> T getBean(String s, Class<T> type) {
        return CONTEXT.getBean(s, type);
    }

    public static <T> T getBean(Class<T> type) {
        return CONTEXT.getBean(type);
    }


    public static <T> Collection<T> getBeansByTypeAndAnnotation(
            final Class<T> clazz,
            final Class<? extends Annotation> annotationType
    ) {
        final Map<String, T> typedBeans = CONTEXT.getBeansOfType(clazz);
        final Map<String, Object> annotatedBeans = CONTEXT.getBeansWithAnnotation(annotationType);

        typedBeans.keySet().retainAll(annotatedBeans.keySet());
        return typedBeans.values();
    }

    public static <T> T getBeanByQualifier(final Class<T> clazz, final String qualifier) {
        final AutowireCapableBeanFactory beanFactory = CONTEXT
                .getAutowireCapableBeanFactory();

        return BeanFactoryAnnotationUtils.qualifiedBeanOfType(beanFactory, clazz, qualifier);
    }

}
