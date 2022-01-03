package com.api.security;

import com.spring.ApplicationContextFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.security.access.expression.ExpressionUtils;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.core.Authentication;
import org.springframework.security.util.SimpleMethodInvocation;

import java.lang.reflect.Method;

public class SecurityChecker {

    static Logger logger = LoggerFactory.getLogger(SecurityChecker.class);

    //----------------------------------------------START SecurityObject-----------------------------------------------
    private static class SecurityObject {
        public void triggerCheck() { /*NOP*/ }
    }

    private static final Method triggerCheckMethod;
    private static final SecurityObject securityObject = new SecurityObject();

    static {
        Method method = null;
        try {
            method = securityObject.getClass().getMethod("triggerCheck");
        } catch (NoSuchMethodException e) {
            //IGNORE
        }
        triggerCheckMethod = method;
    }
    //----------------------------------------------END SecurityObject-----------------------------------------------


    private static final SpelExpressionParser parser = new SpelExpressionParser();
    private static final MethodSecurityExpressionHandler expressionHandler = createExpressionHandler();


    public static boolean hasAuthorities(Authentication authentication, String securityExpression) {
        if (logger.isDebugEnabled()) {
            logger.debug("Checking security expression [" + securityExpression + "]...");
        }

        final EvaluationContext evaluationContext = expressionHandler.createEvaluationContext(
                authentication, new SimpleMethodInvocation(securityObject, triggerCheckMethod));

        final boolean checkResult = ExpressionUtils.evaluateAsBoolean(
                parser.parseExpression(securityExpression), evaluationContext);

        if (logger.isDebugEnabled()) {
            logger.debug("Check result: " + checkResult);
        }

        return checkResult;
    }

    protected static MethodSecurityExpressionHandler createExpressionHandler() {
        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
        //expressionHandler.setPermissionEvaluator(new CustomPermissionEvaluator());// NOT needed yet

        expressionHandler.setApplicationContext(ApplicationContextFactory.getSpringApplicationContext());
        //expressionHandler.setApplicationContext(ContextLoader.getCurrentWebApplicationContext()); //Alternative solution to retrieve bean

        return expressionHandler;
    }
}