package com.token.validation.jwt;


import com.token.validation.signature.SignatureAlgorithm;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class JwtHeaderTest {

    @Test
    void well_formed_jwt_header() {

        final JwtHeader jwtHeader = new JwtHeader(SignatureAlgorithm.RS256, "JWT", "JSON", "12345");

        Assertions.assertEquals(SignatureAlgorithm.RS256, jwtHeader.getAlgorithm());
        Assertions.assertEquals("JWT", jwtHeader.getType());
        Assertions.assertEquals("JSON", jwtHeader.getContentType());
        Assertions.assertEquals("12345", jwtHeader.getKeyId());
    }
}
