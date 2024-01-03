package com.coolpeace.global.jwt.security;

import com.coolpeace.global.jwt.dto.JwtPayload;
import com.coolpeace.global.jwt.exception.JwtInvalidAccessTokenException;
import com.coolpeace.global.jwt.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final JwtUserDetailsService jwtUserDetailsService;
    private final JwtService jwtService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String accessToken = (String) authentication.getCredentials();
        if (jwtService.isAccessTokenBlackListed(accessToken)) {
            throw new JwtInvalidAccessTokenException();
        }
        JwtPayload jwtPayload = jwtService.verifyToken(accessToken);
        JwtPrincipal jwtPrincipal = (JwtPrincipal) jwtUserDetailsService.loadUserByUsername(jwtPayload.email());
        return JwtAuthenticationToken.authenticated(jwtPrincipal);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(JwtAuthenticationToken.class);
    }
}
