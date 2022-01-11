package com.api.config;

import com.api.security.AllowUserIf;
import com.api.security.TokenAuthentication;
import com.api.security.SecurityChecker;
import com.authentication.security.KeyStoreService;
import com.token.validation.auth.AuthUtils;
import com.token.validation.jwt.JwtClaims;
import com.token.validation.jwt.JwtUtil;
import com.token.validation.jwt.exception.TokenValidationException;
import com.token.validation.response.ValidationResponse;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.security.core.Authentication;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import java.lang.reflect.Method;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class SecurityFilter implements ContainerRequestFilter {

    private final KeyStoreService keyStoreService;
    private final ResourceInfo resourceInfo;

    public SecurityFilter(KeyStoreService keyStoreService, ResourceInfo resourceInfo) {
        this.keyStoreService = keyStoreService;
        this.resourceInfo = resourceInfo;
    }


    @Override
    public void filter(final ContainerRequestContext requestContext) {
        final String authorization = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        if (authorization == null) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("{\"error\":\"missing_authorization_header\"}").build());
            return;
        }
        if (!authorization.startsWith("Bearer ")) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("{\"error\":\"invalid_authorization_scheme\"}").build());
            return;
        }

        final PublicKey publicKey;
        try {
            publicKey = keyStoreService.getPublicKey();
            ValidationResponse<JwtClaims> response = JwtUtil.verifyJwt(AuthUtils.extractBearerToken(authorization), publicKey);
            if (!response.isValid()) {
                requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("{\"error\":\"invalid_credentials\"}").build());
                return;
            }

            Method method = resourceInfo.getResourceMethod();
            //Verify user access
            if (method.isAnnotationPresent(RolesAllowed.class)) {
                RolesAllowed rolesAnnotation = method.getAnnotation(RolesAllowed.class);
                Set<String> rolesSet = new HashSet<>(Arrays.asList(rolesAnnotation.value()));

                //Is user allowed ?
                if (!isUserAllowed(response.getClaims(), rolesSet)) {
                    requestContext.abortWith(Response.status(Response.Status.FORBIDDEN).build());

                }
            }
            if (method.isAnnotationPresent(AllowUserIf.class)) {
                AllowUserIf permissionsAnnotation = method.getAnnotation(AllowUserIf.class);
                String securityExpression = permissionsAnnotation.value();
                Authentication authentication = createAuthentication(response.getClaims());

                //Has user required authorities ?
                if (!SecurityChecker.hasAuthorities(authentication, securityExpression)) {
                    requestContext.abortWith(Response.status(Response.Status.FORBIDDEN).build());
                }
            }

        } catch (TokenValidationException exception) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("{\"error\":\"invalid_token_format\"}").build());
        } catch (Exception exception) {
            exception.printStackTrace();
            requestContext.abortWith(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("{\"error\":\"Unable to process your request, due to: \n" + ExceptionUtils.getStackTrace(exception) + "\n\"}").build());
        }
    }

    public static boolean isUserAllowed(JwtClaims claims, final Set<String> rolesSet) {
        boolean isAllowed = false;

        if (claims.getRoles() == null){
            return false;
        }
        String[] roles = claims.getRoles().split(",");

        if (rolesSet.stream().anyMatch(Arrays.asList(roles)::contains)) {
            isAllowed = true;
        }
        return isAllowed;
    }

    private static Authentication createAuthentication(JwtClaims claims) {
        return new TokenAuthentication(claims);
    }
}