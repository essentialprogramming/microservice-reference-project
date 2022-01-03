package com.authentication.security;

import com.api.config.SecurityFilter;
import com.token.validation.jwt.JwtClaims;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class RolesTest {

    @Test
    void user_has_role()  {
        Set<String> roles = new HashSet<>(Arrays.asList("admin", "owner"));
        JwtClaims claims = JwtClaims.builder()
                .roles("admin, visitor")
                .build();

        Assertions.assertTrue(SecurityFilter.isUserAllowed(claims, roles));
    }
}
