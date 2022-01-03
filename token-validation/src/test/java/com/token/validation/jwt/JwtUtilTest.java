package com.token.validation.jwt;

import com.token.validation.auth.AuthUtils;
import com.token.validation.jwt.exception.JwtTokenMissingException;
import com.token.validation.jwt.exception.TokenValidationException;
import com.token.validation.response.ValidationResponse;
import com.token.validation.signature.SignatureAlgorithm;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertThrows;

class JwtUtilTest {

    private KeyPair keyPair;
    private SecretKey secretKey;

    @BeforeEach
    void setUp() throws NoSuchAlgorithmException {
        keyPair = generateRSAKeyPair();
        secretKey = generateHmacSecretKey();
    }

    @Test
    void verify_token_signed_with_RS256() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, TokenValidationException {

        final Instant now = Instant.now();
        long secondsSinceEpoch = Date.from(now).getTime() / 1000L;
        long expiration = secondsSinceEpoch + 300;

        String jwt = createJWT(SignatureAlgorithm.RS256, expiration);
        ValidationResponse<JwtClaims> response = JwtUtil.verifyJwt(jwt, keyPair.getPublic());
        Assertions.assertTrue(response.isValid());
    }

    @Test
    void verify_token_signed_with_HS256() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, TokenValidationException {

        final Instant now = Instant.now();
        long secondsSinceEpoch = Date.from(now).getTime() / 1000L;
        long expiration = secondsSinceEpoch + 300;

        String jwt = createJWT(SignatureAlgorithm.HS256, expiration);
        ValidationResponse<JwtClaims> response = JwtUtil.verifyJwt(jwt, secretKey);
        Assertions.assertTrue(response.isValid());
    }

    @Test
    void verify_expired_token() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, TokenValidationException {

        final Instant now = Instant.now();
        long secondsSinceEpoch = Date.from(now).getTime() / 1000L;
        long expiration = secondsSinceEpoch - 300;

        String jwt = createJWT(SignatureAlgorithm.RS256, expiration);
        ValidationResponse<JwtClaims> response = JwtUtil.verifyJwt(jwt, keyPair.getPublic());
        Assertions.assertFalse(response.isValid());
    }


    @Test
    void get_claims_successfully() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {

        final Instant now = Instant.now();
        long secondsSinceEpoch = Date.from(now).getTime() / 1000L;
        long expiration = secondsSinceEpoch + 300;

        String token = createJWT(SignatureAlgorithm.RS256, expiration);
        String currentClaims =
                new String(Base64.getUrlDecoder().decode(token.split("\\.")[1]), StandardCharsets.UTF_8);

        final Jwt jwt = Jwt.parse(token);
        String claims = jwt.getContent();
        Assertions.assertEquals(currentClaims, claims);
    }

    @Test
    void get_jwt_from_authorization_header_successfully() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {

        final Instant now = Instant.now();
        long secondsSinceEpoch = Date.from(now).getTime() / 1000L;
        long expiration = secondsSinceEpoch + 300;

        String jwt = createJWT(SignatureAlgorithm.RS256, expiration);
        String authorizationHeader = "Bearer " + jwt;

        String result = AuthUtils.extractBearerToken(authorizationHeader);
        Assertions.assertEquals(jwt, result);
    }

    @Test
    void get_jwt_from_authorization_header_fails() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {

        final Instant now = Instant.now();
        long secondsSinceEpoch = Date.from(now).getTime() / 1000L;
        long expiration = secondsSinceEpoch + 300;

        final String jwt = createJWT(SignatureAlgorithm.RS256, expiration);
        String authorizationToken = "Basic " + jwt;

        assertThrows(JwtTokenMissingException.class, () -> AuthUtils.extractBearerToken(authorizationToken));
    }


    @Test
    void get_jwt_from_authorization_header_fails_if_whitespace_is_missing() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {

        final Instant now = Instant.now();
        long secondsSinceEpoch = Date.from(now).getTime() / 1000L;
        long expiration = secondsSinceEpoch + 300;

        String jwt = createJWT(SignatureAlgorithm.RS256, expiration);
        String authorizationToken = "Bearer" + jwt;

        assertThrows(JwtTokenMissingException.class, () -> AuthUtils.extractBearerToken(authorizationToken));
    }

    @Test
    void get_key_id_successfully() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, TokenValidationException {

        final Instant now = Instant.now();
        long secondsSinceEpoch = Date.from(now).getTime() / 1000L;
        long expiration = secondsSinceEpoch + 300;

        String jwt = createJWT(SignatureAlgorithm.RS256, expiration);

        Assertions.assertEquals("123456", JwtUtil.getKeyId(jwt));
    }

    private String createJWT(SignatureAlgorithm signatureAlgorithm, Long expirationTime)
            throws SignatureException, InvalidKeyException, NoSuchAlgorithmException {

        // create header
        String header = "{ \"alg\" : \"" + signatureAlgorithm.name() + "\", \"typ\" : \"JWT\", \"kid\" : \"123456\" }";

        // create payload
        String payload = "{ \"iss\" : \"issuer\", \"exp\" : " + expirationTime + " }";

        // create jwt
        StringBuilder jwt = new StringBuilder();
        jwt.append(Base64.getUrlEncoder().encodeToString(header.getBytes()));
        jwt.append(".");
        jwt.append(Base64.getUrlEncoder().encodeToString(payload.getBytes()));

        // sign jwt
        byte[] signature = new byte[0];
        if (signatureAlgorithm.isRsa()) {
            PrivateKey key = keyPair.getPrivate();
            signature = signRSA(jwt.toString().getBytes(), key, signatureAlgorithm.getJcaName());
        }
        if (signatureAlgorithm.isHmac()) {
            signature = signHMAC(jwt.toString().getBytes(), signatureAlgorithm.getJcaName(), secretKey);
        }
        String encodedSignature = Base64.getUrlEncoder().encodeToString(signature);

        jwt.append(".");
        jwt.append(encodedSignature);

        return jwt.toString();
    }

    private static byte[] signRSA(byte[] data, PrivateKey privateKey, String alg) throws InvalidKeyException, SignatureException, NoSuchAlgorithmException {

        final Signature signature = Signature.getInstance(alg);
        signature.initSign(privateKey);
        signature.update(data);
        return signature.sign();
    }

    private static byte[] signHMAC(byte[] data, String algorithm, SecretKey key) throws InvalidKeyException, NoSuchAlgorithmException {
        Mac mac = Mac.getInstance(algorithm);
        mac.init(key);
        mac.update(data);
        return mac.doFinal();
    }

    private static KeyPair generateRSAKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        return keyGen.generateKeyPair();
    }

    private static SecretKey generateHmacSecretKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA1");
        return keyGen.generateKey();
    }

}
