package com.token.validation.auth;

import com.token.validation.basic.BadCredentialsException;
import com.token.validation.jwt.Jwt;
import com.token.validation.jwt.JwtClaims;
import com.token.validation.jwt.exception.JwtTokenMissingException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class AuthUtils {

    private static final Pattern authorizationPattern = Pattern.compile("^Bearer$", Pattern.CASE_INSENSITIVE);
    private static final Pattern basicAuthorizationPattern = Pattern.compile("^Basic$", Pattern.CASE_INSENSITIVE);

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

            Matcher matcher = authorizationPattern.matcher(scheme);
            if (!matcher.matches()) {
                throw new JwtTokenMissingException("Invalid authorization scheme");
            }
            jwt = credentials;

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

    public static String createBasicAuthenticationHeader(String username, String password) {
        return "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8));
    }

    public static Map<String, String> extractBasicUsernameAndPassword(String authorizationHeader) {
        if (authorizationHeader == null || authorizationHeader.isEmpty())
            throw new BadCredentialsException("Missing authorization header");

        authorizationHeader = authorizationHeader.trim();

        final Map<String, String> usernamePassword = new HashMap<>();
        final String[] parts = authorizationHeader.split(" ");
        if (parts.length == 2) {
            String scheme = parts[0];
            Matcher matcher = basicAuthorizationPattern.matcher(scheme);
            if (!matcher.matches()) {
                throw new BadCredentialsException("Invalid authorization scheme");
            }

            byte[] base64Token = parts[1].getBytes(StandardCharsets.UTF_8);
            byte[] decoded = AuthUtils.decode(base64Token);
            String token = new String(decoded, StandardCharsets.UTF_8);
            int delimiter = token.indexOf(":");
            if (delimiter == -1) {
                throw new BadCredentialsException("Invalid basic authentication token");
            } else {
                usernamePassword.putIfAbsent("username", token.substring(0, delimiter));
                usernamePassword.putIfAbsent("password", token.substring(delimiter + 1));
            }

        } else {
            throw new BadCredentialsException("No BASIC token found in request headers");
        }
        return usernamePassword;
    }

    private static byte[] decode(byte[] base64Token) {
        try {
            return Base64.getDecoder().decode(base64Token);
        } catch (IllegalArgumentException exception) {
            throw new BadCredentialsException("Failed to decode basic authentication token");
        }
    }
}
