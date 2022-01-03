package com.token.validation.jwt;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.token.validation.jwt.exception.TokenValidationException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Jwt {

    private final String base64Header;
    private final String base64Content;
    private final String base64Signature;

    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    private Jwt(final String input) throws TokenValidationException {

        final String[] parts = input.split("\\.");
        if (parts.length < 2)
            throw new TokenValidationException("JWT string '" + input + "' is missing a body/payload.");
        if (parts.length > 3)
            throw new TokenValidationException("JWT strings must contain exactly 2 period characters. Found: " + (parts.length - 1));

        base64Header = parts[0];
        base64Content = parts[1];
        base64Signature = isDigitallySigned(input) ? parts[2] : null;
    }

    public static Jwt parse(final String input) {
        if (input == null || input.trim().length() == 0) {
            throw new TokenValidationException("JWT string cannot be null or empty");
        }
        int firstDotPosition = input.indexOf(".");
        if (firstDotPosition == -1)
            throw new TokenValidationException("Invalid JWT serialization: Missing dot delimiter(s)");

        return new Jwt(input);
    }

    public String getEncodedHeader() {
        return base64Header;
    }

    public String getEncodedContent() {
        return base64Content;
    }

    public String getEncodedSignature() {
        return base64Signature;
    }

    public String getHeaderText() {
        byte[] headerBytes = Base64.getUrlDecoder().decode(base64Header);
        return new String(headerBytes, StandardCharsets.UTF_8);
    }

    public String getContent() {
        return new String(Base64.getUrlDecoder().decode(base64Content), StandardCharsets.UTF_8);
    }

    public byte[] getSignature() {
        return Base64.getUrlDecoder().decode(base64Signature);
    }

    public boolean isDigitallySigned() {
        return base64Signature != null;
    }

    public JwtHeader getHeader() throws IOException {
        byte[] headerBytes = Base64.getUrlDecoder().decode(base64Header);
        return mapper.readValue(headerBytes, JwtHeader.class);
    }

    public JwtClaims getClaims() throws IOException {
        byte[] claimsBytes = Base64.getUrlDecoder().decode(base64Content);
        return mapper.readValue(claimsBytes, JwtClaims.class);
    }

    private static boolean isDigitallySigned(String jwt) {
        String[] parts = jwt.split("\\.");
        return parts.length > 2;
    }

}
