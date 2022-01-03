package com.api.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;

@Aspect
@Component
public class ExampleAspect {

    @Around("@annotation(org.springframework.transaction.annotation.Transactional)")
    //@Around("com.config.AOPConfig.serviceOperation())")
    public Object aroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        System.out.println("Method name:" + method.getName());

        System.out.print("Method arguments:");
        Arrays.stream(signature.getParameterNames())
                .forEach(s -> System.out.print(" " + s));
        System.out.println();

        System.out.print("Method argument types:");
        Arrays.stream(signature.getParameterTypes())
                .forEach(s -> System.out.print(" " + s));
        System.out.println();

        System.out.print("Method argument values:");
        Arrays.stream(joinPoint.getArgs())
                .forEach(o -> System.out.print(" " + (o != null ? o.toString() : "")));
        System.out.println();

        return joinPoint.proceed();
    }
}