package com.token.validation.crypto;

import com.token.validation.jwt.Jwt;
import com.token.validation.jwt.exception.TokenValidationException;
import com.token.validation.signature.SignatureAlgorithm;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class EllipticCurveSignatureValidatorTest {


    @Test
    void verifyEC256() throws SignatureException, InvalidKeyException, NoSuchAlgorithmException, TokenValidationException, InvalidAlgorithmParameterException {

        final KeyPair keyPair = generateKeyPair(SignatureAlgorithm.ES256);
        final String jwt = createJWT(SignatureAlgorithm.ES256, keyPair.getPrivate());

        final Jwt jwtToken = Jwt.parse(jwt);

        SignatureVerifier signatureVerifier = EllipticCurveProvider.getInstance();
        Assertions.assertTrue(signatureVerifier.verify(jwtToken, keyPair.getPublic()));
    }

    @Test
    void verifyEC384() throws SignatureException, InvalidKeyException, NoSuchAlgorithmException, TokenValidationException, InvalidAlgorithmParameterException {

        final KeyPair keyPair = generateKeyPair(SignatureAlgorithm.ES384);
        final String jwt = createJWT(SignatureAlgorithm.ES384, keyPair.getPrivate());

        final Jwt jwtToken = Jwt.parse(jwt);

        SignatureVerifier signatureVerifier = EllipticCurveProvider.getInstance();
        Assertions.assertTrue(signatureVerifier.verify(jwtToken, keyPair.getPublic()));
    }

    @Test
    void verifyEC512() throws SignatureException, InvalidKeyException, NoSuchAlgorithmException, TokenValidationException, InvalidAlgorithmParameterException {

        final KeyPair keyPair = generateKeyPair(SignatureAlgorithm.ES512);
        final String jwt = createJWT(SignatureAlgorithm.ES256, keyPair.getPrivate());

        final Jwt jwtToken = Jwt.parse(jwt);

        SignatureVerifier signatureVerifier = EllipticCurveProvider.getInstance();
        Assertions.assertTrue(signatureVerifier.verify(jwtToken, keyPair.getPublic()));
    }

    @Test
    void missing_algorithm_will_raise_an_validation_exception() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, TokenValidationException, InvalidAlgorithmParameterException {

        final KeyPair keyPair = generateKeyPair(SignatureAlgorithm.NONE);
        final String jwt = createJWT(SignatureAlgorithm.NONE, keyPair.getPrivate());

        final Jwt jwtToken = Jwt.parse(jwt);

        SignatureVerifier signatureVerifier = EllipticCurveProvider.getInstance();
        Exception exception = assertThrows(TokenValidationException.class, () -> signatureVerifier.verify(jwtToken, keyPair.getPublic()));
        Assertions.assertTrue(exception.getMessage().contains("Something went wrong on signature verification"));
    }

    @Test
    void jwt_is_not_valid_if_signature_key_is_invalid() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, TokenValidationException, InvalidAlgorithmParameterException {

        final KeyPair keyPair = generateKeyPair(SignatureAlgorithm.ES256);
        final String jwt = createJWT(SignatureAlgorithm.ES256, keyPair.getPrivate());

        final Jwt jwtToken = Jwt.parse(jwt);

        KeyPair keys = generateKeyPair(SignatureAlgorithm.ES256);
        SignatureVerifier signatureVerifier = EllipticCurveProvider.getInstance();
        Assertions.assertFalse(signatureVerifier.verify(jwtToken, keys.getPublic()));
    }

    private static String createJWT(SignatureAlgorithm signatureAlgorithm, PrivateKey privateKey)
            throws SignatureException, InvalidKeyException, NoSuchAlgorithmException {

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
        if (signatureAlgorithm.isEllipticCurve()){
            byte[] signature = sign(jwt.toString().getBytes(), privateKey, signatureAlgorithm.getJcaName());
            String encodedSignature = Base64.getUrlEncoder().encodeToString(signature);

            jwt.append(".");
            jwt.append(encodedSignature);
        }


        return jwt.toString();
    }

    private static byte[] sign(byte[] data, PrivateKey privateKey, String alg) throws InvalidKeyException, SignatureException, NoSuchAlgorithmException {
        final Signature signature = Signature.getInstance(alg);
        signature.initSign(privateKey);
        signature.update(data);
        return signature.sign();
    }

    private KeyPair generateKeyPair(SignatureAlgorithm alg) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC");
        String paramSpecCurveName = EllipticCurveProvider.ELLIPTIC_CURVE_NAMES.getOrDefault(alg, "secp256r1");
        keyGen.initialize(new ECGenParameterSpec(paramSpecCurveName));
        return keyGen.generateKeyPair();
    }
}