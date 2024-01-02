package com.coolpeace.global.jwt.security;

import com.coolpeace.global.jwt.exception.InvalidAuthorizationHeaderException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

public class JwtAuthenticationConverter implements AuthenticationConverter {
    private static final String AUTHENTICATION_SCHEME_JWT = "Bearer";
    public static final String JWT_REGEX = "^([a-zA-Z0-9_=]+)\\.([a-zA-Z0-9_=]+)\\.([a-zA-Z0-9_\\-\\+\\/=]*)";

    private final AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource;

    public JwtAuthenticationConverter() {
        this(new WebAuthenticationDetailsSource());
    }

    public JwtAuthenticationConverter(AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource) {
        this.authenticationDetailsSource = authenticationDetailsSource;
    }

    @Override
    public JwtAuthenticationToken convert(HttpServletRequest request) {
        String rawAuthHeaderValue = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (rawAuthHeaderValue == null) {
            return null;
        }

        String authHeaderValue = rawAuthHeaderValue.trim();

        if (!validateAuthHeader(authHeaderValue)) {
            throw new InvalidAuthorizationHeaderException();
        }

        String requestAccessToken = authHeaderValue.substring(AUTHENTICATION_SCHEME_JWT.length() + 1);
        if (Pattern.matches(JWT_REGEX, requestAccessToken)) {
            JwtAuthenticationToken result = JwtAuthenticationToken.unauthenticated(requestAccessToken);
            result.setDetails(this.authenticationDetailsSource.buildDetails(request));
            return result;
        }

        return null;
    }

    private static boolean validateAuthHeader(String authHeaderValue) {
        return !StringUtils.startsWithIgnoreCase(authHeaderValue, AUTHENTICATION_SCHEME_JWT)
                && authHeaderValue.equalsIgnoreCase(AUTHENTICATION_SCHEME_JWT);
    }

}