package com.coolpeace.api.global.jwt.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private JwtPrincipal jwtPrincipal;
    private final String credentials;

    public JwtAuthenticationToken(JwtPrincipal jwtPrincipal, String credentials, boolean isAuthenticated) {
        super(jwtPrincipal.getAuthorities());
        this.jwtPrincipal = jwtPrincipal;
        this.credentials = credentials;
        setAuthenticated(isAuthenticated);
    }

    public JwtAuthenticationToken(String credentials, boolean isAuthenticated) {
        super(AuthorityUtils.NO_AUTHORITIES);
        this.credentials = credentials;
        setAuthenticated(isAuthenticated);
    }

    public static JwtAuthenticationToken unauthenticated(String accessToken) {
        return new JwtAuthenticationToken(accessToken,false);
    }

    public static JwtAuthenticationToken authenticated(JwtPrincipal jwtPrincipal, String accessToken) {
        return new JwtAuthenticationToken(jwtPrincipal, accessToken, true);
    }

    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    @Override
    public Object getPrincipal() {
        return this.jwtPrincipal;
    }
}
