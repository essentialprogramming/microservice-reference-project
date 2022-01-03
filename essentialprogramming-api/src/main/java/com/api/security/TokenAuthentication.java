package com.api.security;

import com.token.validation.auth.AuthUtils;
import com.token.validation.jwt.JwtClaims;
import com.util.collection.CollectionUtils;
import org.springframework.security.authentication.AbstractAuthenticationToken;

public class TokenAuthentication extends AbstractAuthenticationToken {

    private static final long serialVersionUID = 1L;
    private final String principal;

    public TokenAuthentication(JwtClaims claims) {
        super(CollectionUtils.concat(PermissionsConverter.convert(claims), RolesConverter.convert(claims)));
        this.principal = AuthUtils.getClaim(claims, "email");
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public Object getCredentials() {
        return null;
    }
}
