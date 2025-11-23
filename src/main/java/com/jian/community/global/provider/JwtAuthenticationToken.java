package com.jian.community.global.provider;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    Object credentials;
    final Object principal;

    // 미인증 토큰
    public JwtAuthenticationToken(Object credentials) {
        super(null);
        this.credentials = credentials;
        this.principal = null;
        setAuthenticated(false);
    }

    // 인증 토큰
    public JwtAuthenticationToken(
            Object credentials,
            Object principal,
            Collection<? extends GrantedAuthority> authorities
    ) {
        super(authorities);
        this.credentials = credentials;
        this.principal = principal;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }
}
