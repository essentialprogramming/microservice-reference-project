package com.token.validation.jwt;

import com.token.validation.jwt.exception.TokenValidationException;
import com.token.validation.signature.SignatureAlgorithm;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.*;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JwtTest {

    private KeyPair keyPair;

    @BeforeEach
    void setUp() throws NoSuchAlgorithmException {
        keyPair = generateRSAKeyPair();
    }

    @Test
    void create_and_sign_jwt_successfully() throws SignatureException, InvalidKeyException, NoSuchAlgorithmException, TokenValidationException {
        // create header
        String header = "{ \"alg\" : \"RS256\", \"typ\" : \"JWT\" }";
        String encodedHeader = Base64.getUrlEncoder().encodeToString(header.getBytes());

        // create payload
        Instant now = Instant.now();
        long secondsSinceEpoch = Date.from(now).getTime() / 1000L;
        String payload = "{ \"iss\" : \"issuer\", \"iat\" : \"" + secondsSinceEpoch + "\" }";
        String encodedPayload = Base64.getUrlEncoder().encodeToString(payload.getBytes());

        // create jwt
        StringBuilder jwt = new StringBuilder();
        jwt.append(encodedHeader);
        jwt.append(".");
        jwt.append(encodedPayload);

        // sign jwt
        PrivateKey privateKey = keyPair.getPrivate();

        byte[] sign = sign(jwt.toString().getBytes(), privateKey, SignatureAlgorithm.RS256.getJcaName());
        String encodedSignature = Base64.getUrlEncoder().encodeToString(sign);

        jwt.append(".");
        jwt.append(encodedSignature);

        final Jwt jwtToken = Jwt.parse(jwt.toString());

        Assertions.assertEquals(encodedHeader, jwtToken.getEncodedHeader());
        Assertions.assertEquals(encodedPayload, jwtToken.getEncodedContent());
        Assertions.assertEquals(encodedSignature, jwtToken.getEncodedSignature());
        Assertions.assertArrayEquals(sign, jwtToken.getSignature());
        Assertions.assertEquals(payload, jwtToken.getContent());
    }

    @Test
    void jwt_with_missing_signature_is_not_digitally_signed() throws TokenValidationException {

        String header = "{ \"alg\" : \"RS256\", \"typ\" : \"JWT\" }";
        String encodedHeader = Base64.getUrlEncoder().encodeToString(header.getBytes());

        // create payload
        Instant now = Instant.now();
        long secondsSinceEpoch = Date.from(now).getTime() / 1000L;
        String payload = "{ \"iss\" : \"issuer\", \"iat\" : \"" + secondsSinceEpoch + "\" }";
        String encodedPayload = Base64.getUrlEncoder().encodeToString(payload.getBytes());

        // create jwt
        String jwt = encodedHeader +
                "." +
                encodedPayload;
        Jwt jwtToken =  Jwt.parse(jwt);
        assertFalse(jwtToken.isDigitallySigned());
    }

    @Test
    void missing_dot_delimiter_will_raise_an_validation_exception() throws TokenValidationException {

        String header = "{ \"alg\" : \"RS256\", \"typ\" : \"JWT\" }";
        String encodedHeader = Base64.getUrlEncoder().encodeToString(header.getBytes());

        // create jwt
        StringBuilder jwt = new StringBuilder();
        jwt.append(encodedHeader);

        Exception exception = assertThrows(TokenValidationException.class, () ->  Jwt.parse(jwt.toString()));
        Assertions.assertTrue(exception.getMessage().contains("Invalid JWT serialization: Missing dot delimiter(s)"));
    }

    @Test
    void jwt_string_composed_of_more_than_3_parts_is_invalid() throws TokenValidationException {

        String jwtPart1 = "{ \"alg\" : \"RS256\", \"typ\" : \"JWT\" }";
        String encodedJwtPart1 = Base64.getUrlEncoder().encodeToString(jwtPart1.getBytes());

        String jwtPart2 = "{ \"iss\" : \"issuer\", \"exp\" : " + 2592000 + " }";
        String encodedJwtPart2 = Base64.getUrlEncoder().encodeToString(jwtPart2.getBytes());

        String jwtPart3 = "{ \"iss\" : \"issuer\", \"exp\" : " + 2592000 + " }";
        String encodedJwtPart3 = Base64.getUrlEncoder().encodeToString(jwtPart3.getBytes());

        String jwtPart4 = "{ \"iss\" : \"issuer\", \"exp\" : " + 2592000 + " }";
        String encodedJwtPart4 = Base64.getUrlEncoder().encodeToString(jwtPart4.getBytes());


        // create jwt
        String jwt = encodedJwtPart1 + "." + encodedJwtPart2 + "." + encodedJwtPart3 + "." + encodedJwtPart4;

        Exception exception = assertThrows(TokenValidationException.class, () ->  Jwt.parse(jwt));
        Assertions.assertTrue(exception.getMessage().contains("JWT strings must contain exactly 2 period characters. Found:"));
    }


    private static byte[] sign(byte[] data, PrivateKey privateKey, String alg) throws InvalidKeyException, SignatureException, NoSuchAlgorithmException {

        final Signature signature = Signature.getInstance(alg);
        signature.initSign(privateKey);
        signature.update(data);
        return signature.sign();
    }

    private KeyPair generateRSAKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        return keyGen.generateKeyPair();
    }
}
