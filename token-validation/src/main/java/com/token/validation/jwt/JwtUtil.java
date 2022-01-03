package com.token.validation.jwt;

import com.token.validation.crypto.EllipticCurveProvider;
import com.token.validation.crypto.HMACProvider;
import com.token.validation.crypto.RSAProvider;
import com.token.validation.crypto.SignatureVerifier;
import com.token.validation.jwt.exception.TokenValidationException;
import com.token.validation.response.ValidationResponse;

import java.io.IOException;
import java.security.Key;
import java.time.Instant;
import java.util.Date;

public class JwtUtil {

    private static final SignatureVerifier signatureVerifier = (jwt, key) -> {
        try {
            switch (jwt.getHeader().getAlgorithm().getSignatureType()) {
                case "RSA":
                    return RSAProvider.getInstance().verify(jwt, key);
                case "HMAC":
                    return HMACProvider.getInstance().verify(jwt, key);
                case "ECDSA":
                    return EllipticCurveProvider.getInstance().verify(jwt, key);
                default:
                    return false;
            }
        } catch (IOException e) {
            throw new TokenValidationException("Cannot parse header", e);
        }

    };

    private JwtUtil() {
    }


    /**
     * Verify JWT token format, signature and expiration time.
     *
     * @param jwt The JWT token Base64 encoded
     * @param key The key used to check signature
     * @return ValidationResponse
     * @throws TokenValidationException If the JWT token has invalid format or is otherwise malformed,
     *                                  the key is null, the signature can't be verified.
     */
    public static ValidationResponse<JwtClaims> verifyJwt(final String jwt, final Key key)
            throws TokenValidationException {

        if (key == null) {
            throw new TokenValidationException("Key must not be null");
        }
        final Jwt jwtToken = Jwt.parse(jwt);
        final JwtClaims claims;

        boolean isSignatureValid;
        boolean isExpired;

        try {
            isSignatureValid = signatureVerifier.verify(jwtToken, key);
            claims = jwtToken.getClaims();

            Instant now = Instant.now();
            long secondsSinceEpoch = Date.from(now).getTime() / 1000L;
            isExpired = (secondsSinceEpoch >= claims.getExpiration());

        } catch (IOException e) {
            throw new TokenValidationException("Token validation failed");
        }

        return new ValidationResponse<>(isSignatureValid  && !isExpired , claims);
    }

    /**
     * Get key id from JWT token. This id will be used to identity the right Public Key to verify the
     * signature.
     *
     * @param jwt The JWT token Base64 encoded
     * @return key id which can be use to validate signature
     * @throws TokenValidationException If the JWT token has invalid format or is null
     */
    public static String getKeyId(String jwt) throws TokenValidationException {
        try {
            Jwt jwtToken = Jwt.parse(jwt);
            JwtHeader header = jwtToken.getHeader();
            return header.getKeyId();
        } catch (IOException e) {
            throw new TokenValidationException("Error parsing JWT");
        }
    }
}
