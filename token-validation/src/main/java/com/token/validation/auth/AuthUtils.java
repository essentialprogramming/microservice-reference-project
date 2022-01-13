package com.token.validation.auth;

import com.token.validation.jwt.Jwt;
import com.token.validation.jwt.JwtClaims;
import com.token.validation.jwt.exception.JwtTokenMissingException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.regex.Pattern;

public final class AuthUtils {

    private AuthUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Parse the jwt token from Authorization header.
     *
     * @param authorizationHeader authorization header.
     * @return JWT token
     */
    public static String extractBearerToken(String authorizationHeader) {
        if (authorizationHeader == null || authorizationHeader.isEmpty())
            throw new JwtTokenMissingException("Missing authorization header");

        final String jwt;
        final String[] parts = authorizationHeader.split(" ");
        if (parts.length == 2) {
            String scheme = parts[0];
            String credentials = parts[1];
            Pattern pattern = Pattern.compile("^Bearer$", Pattern.CASE_INSENSITIVE);
            if (pattern.matcher(scheme).matches()) {
                jwt = credentials;
            } else {
                throw new JwtTokenMissingException("Invalid authorization scheme");
            }
        } else {
            throw new JwtTokenMissingException("No JWT token found in request headers");
        }
        return jwt;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getClaim(final String jwtToken, final String key) {
        final Jwt token = Jwt.parse(jwtToken);
        try {
            JwtClaims claims = token.getClaims();
            return claims.containsKey(key) ? ((T) claims.get(key)) : null;
        } catch (IOException e) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T getClaim(JwtClaims claims, String key) {
        return claims.containsKey(key) ? ((T) claims.get(key)) : null;

    }

    public static String createBasicAuthenticationHeader(String username, String password){
        // set http basic auth for client
        return "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8));
    }
}
