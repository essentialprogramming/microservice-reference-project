package com.token.validation.crypto;

import com.token.validation.jwt.Jwt;
import com.token.validation.jwt.exception.SignatureVerificationException;

import java.security.Key;

public interface SignatureVerifier {

    /**
     * Verify the given token using the provided algorithm in the JWT header
     *
     * @param input the JWT that it's going to be verified.
     * @throws SignatureVerificationException if the Token's Signature is invalid, meaning that it doesn't match the signatureBytes, or if the Key is invalid.
     */
    boolean verify(final Jwt input, final Key key) throws SignatureVerificationException;
}
