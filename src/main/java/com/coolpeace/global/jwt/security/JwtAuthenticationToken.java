package com.coolpeace.global.jwt.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private MemberPrincipal memberPrincipal;
    private final String credentials;

    public JwtAuthenticationToken(MemberPrincipal memberPrincipal, String credentials, boolean isAuthenticated) {
        super(memberPrincipal.getAuthorities());
        this.memberPrincipal = memberPrincipal;
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

    public static JwtAuthenticationToken authenticated(MemberPrincipal memberPrincipal, String accessToken) {
        return new JwtAuthenticationToken(memberPrincipal, accessToken, true);
    }

    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    @Override
    public Object getPrincipal() {
        return this.memberPrincipal;
    }
}
