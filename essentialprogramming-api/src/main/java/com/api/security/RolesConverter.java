package com.api.security;

import com.token.validation.jwt.JwtClaims;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class RolesConverter {

    public static Collection<GrantedAuthority> convert(JwtClaims claims) {
        String[] roles = claims.getRoles() != null ? claims.getRoles().split("\\,") : new String[]{};
        return Arrays.stream(roles)
                .map(roleName -> "ROLE_" + roleName)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}