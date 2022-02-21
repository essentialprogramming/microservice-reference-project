package com.api.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

@Aspect
@Component
@Slf4j
public class ExampleAspect {

    @Around("@annotation(org.springframework.transaction.annotation.Transactional)")
    //@Around("com.config.AOPConfig.serviceOperation())")
    public Object aroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();


        final String logMessage = System.getProperty("line.separator") +
                "       Method name: " + method.getName() +

                System.getProperty("line.separator") +
                "       Method arguments:" +
                String.join(", ", signature.getParameterNames()) +

                System.getProperty("line.separator") +
                "       Method argument types:" +
                Arrays.stream(signature.getParameterTypes())
                        .filter(Objects::nonNull)
                        .map(Class::toString)
                        .collect(Collectors.joining(", ")) +

                System.getProperty("line.separator") +
                "       Method argument values:" +
                Arrays.stream(joinPoint.getArgs())
                        .filter(Objects::nonNull)
                        .map(Object::toString)
                        .collect(Collectors.joining(", ")) +

                System.getProperty("line.separator");

        log.warn(logMessage);

        return joinPoint.proceed();
    }
}