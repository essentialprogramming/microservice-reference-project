package com.config;

import com.spring.ApplicationContextFactory;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jobrunr.scheduling.JobScheduler;
import org.jobrunr.scheduling.RecurringJobProcessor;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.stereotype.Component;
import org.springframework.util.StringValueResolver;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;

@Component
@RequiredArgsConstructor
@Slf4j
public class RecurringJobConfig implements EmbeddedValueResolverAware {

    private StringValueResolver embeddedValueResolver;
    private final JobScheduler jobScheduler;


    @PostConstruct
    public void initRecurringJobs() {
        final RecurringJobProcessor recurringJobProcessor =
                new RecurringJobProcessor(jobScheduler, embeddedValueResolver);
        final ApplicationContext applicationContext =
                ApplicationContextFactory.getSpringApplicationContext();

        for (String beanName : applicationContext.getBeanDefinitionNames()) {
            final Method[] methods = AopUtils.getTargetClass(applicationContext.getBean(beanName)).getDeclaredMethods();
            for (final Method method : methods) {
                recurringJobProcessor.doWith(method);
            }
        }
    }

    @Override
    public void setEmbeddedValueResolver(@NonNull StringValueResolver resolver) {
        this.embeddedValueResolver = resolver;
    }
}