package com.api.security;

import com.token.validation.jwt.JwtClaims;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.stream.Collectors;

public class PermissionsConverter {

    public static Collection<GrantedAuthority> convert(JwtClaims claims) {
        return claims.getPermissions().stream()
                .map(permissionName -> "PERMISSION_" + permissionName )
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}