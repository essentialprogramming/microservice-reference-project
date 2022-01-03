package com.token.validation.crypto;

import com.token.validation.jwt.Jwt;
import com.token.validation.jwt.exception.TokenValidationException;
import com.token.validation.signature.SignatureAlgorithm;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertThrows;

class HMACSignatureValidatorTest {

    private static SecretKey secretKey;

    @BeforeEach
    void setUp() throws NoSuchAlgorithmException {
        secretKey = generateHmacSecretKey();
    }

    @Test
    void verifyHS256() throws NoSuchAlgorithmException, InvalidKeyException, TokenValidationException {

        final String jwt = createJWT(SignatureAlgorithm.HS256);
        final Jwt jwtToken = Jwt.parse(jwt);

        SignatureVerifier signatureVerifier = HMACProvider.getInstance();
        Assertions.assertTrue(signatureVerifier.verify(jwtToken, secretKey));
    }

    @Test
    void verifyHS384() throws NoSuchAlgorithmException, InvalidKeyException, TokenValidationException {

        final String jwt = createJWT(SignatureAlgorithm.HS384);
        final Jwt jwtToken = Jwt.parse(jwt);

        SignatureVerifier signatureVerifier = HMACProvider.getInstance();
        Assertions.assertTrue(signatureVerifier.verify(jwtToken, secretKey));
    }

    @Test
    void verifyHS512() throws NoSuchAlgorithmException, InvalidKeyException, TokenValidationException {

        final String jwt = createJWT(SignatureAlgorithm.HS512);
        final Jwt jwtToken = Jwt.parse(jwt);

        SignatureVerifier signatureVerifier = HMACProvider.getInstance();
        Assertions.assertTrue(signatureVerifier.verify(jwtToken, secretKey));
    }

    @Test
    void missing_algorithm_will_raise_an_validation_exception()
            throws NoSuchAlgorithmException, InvalidKeyException, TokenValidationException {

        final String jwt = createJWT(SignatureAlgorithm.NONE);
        final Jwt jwtToken = Jwt.parse(jwt);

        SignatureVerifier signatureVerifier = HMACProvider.getInstance();
        Exception exception = assertThrows(TokenValidationException.class, () -> signatureVerifier.verify(jwtToken, secretKey));
        Assertions.assertTrue(exception.getMessage().contains("Something went wrong on signature verification"));
    }

    @Test
    void jwt_is_not_valid_if_signature_key_is_invalid() throws NoSuchAlgorithmException, InvalidKeyException, TokenValidationException {

        final String jwt = createJWT(SignatureAlgorithm.HS256);
        final Jwt jwtToken = Jwt.parse(jwt);

        SecretKey newSecretKey = generateHmacSecretKey();
        SignatureVerifier signatureVerifier = HMACProvider.getInstance();
        Assertions.assertFalse(signatureVerifier.verify(jwtToken, newSecretKey));
    }

    private static String createJWT(SignatureAlgorithm signatureAlgorithm)
            throws InvalidKeyException, NoSuchAlgorithmException {

        // create header
        String header = "{ \"alg\" : \"" + signatureAlgorithm.name() + "\", \"typ\" : \"JWT\" }";

        // create payload
        Instant now = Instant.now();
        long secondsSinceEpoch = Date.from(now).getTime() / 1000L;
        String payload = "{ \"iss\" : \"issuer\", \"iat\" : " + secondsSinceEpoch + " }";

        // create jwt
        StringBuilder jwt = new StringBuilder();
        jwt.append(Base64.getUrlEncoder().encodeToString(header.getBytes()));
        jwt.append(".");
        jwt.append(Base64.getUrlEncoder().encodeToString(payload.getBytes()));

        // sign jwt
        if (signatureAlgorithm.isHmac()) {
            byte[] sign = sign(jwt.toString().getBytes(), signatureAlgorithm.getJcaName(), secretKey);
            String encodedSignature = Base64.getUrlEncoder().encodeToString(sign);

            jwt.append(".");
            jwt.append(encodedSignature);
        }
        return jwt.toString();
    }

    private static byte[] sign(byte[] data, String algorithm, SecretKey key) throws InvalidKeyException, NoSuchAlgorithmException {

        final Mac mac = Mac.getInstance(algorithm);
        mac.init(key);
        mac.update(data);
        return mac.doFinal();
    }

    private static SecretKey generateHmacSecretKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA1");
        return keyGen.generateKey();
    }

}
