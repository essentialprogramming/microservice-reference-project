package com.token.validation.jwt;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;

class JwtClaimsTest {

    @Test
    void jwt_claims_are_built_successfully() {
        JwtClaims claims = JwtClaims.builder()
                .issuer("issuer")
                .subject("subject")
                .audience("audience")
                .expiration(2592000)
                .notBefore(1640560612)
                .issuedAt(1643148000)
                .id("id")
                .domain("domain")
                .value("testKey", "testValue")
                .build();

        Assertions.assertEquals("issuer", claims.getIssuer());
        Assertions.assertEquals("subject", claims.getSubject());
        Assertions.assertEquals("audience", claims.getAudience());
        Assertions.assertEquals(2592000, claims.getExpiration());
        Assertions.assertEquals(1640560612, claims.getNotBefore());
        Assertions.assertEquals(1643148000, claims.getIssuedAt());
        Assertions.assertEquals("id", claims.getID());
        Assertions.assertEquals("testValue", claims.get("testKey"));


    }

    @Test
    void jwt_claims_are_copied_successfully() {
        final Map<String, Object> map = new LinkedHashMap<>();
        map.put("iss", "issuer");
        map.put("iat", 1640560612);

        JwtClaims claims = new JwtClaims(map);

        Assertions.assertEquals("issuer", claims.getIssuer());
        Assertions.assertEquals(1640560612, claims.getIssuedAt());
    }

    @Test
    void empty_claims() {
        JwtClaims claims = JwtClaims.builder().build();

        Assertions.assertNull(claims.getIssuer());
        Assertions.assertNull(claims.getSubject());
        Assertions.assertNull(claims.getAudience());
        Assertions.assertNull(claims.getID());
        Assertions.assertNull(claims.getDomain());
        Assertions.assertEquals(0L, claims.getExpiration());
        Assertions.assertEquals(0L, claims.getNotBefore());
        Assertions.assertEquals(0L, claims.getIssuedAt());

    }
}
