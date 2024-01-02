package com.coolpeace.global.jwt.security;

import com.coolpeace.global.exception.ErrorCode;
import com.coolpeace.global.exception.ErrorMessage;
import com.coolpeace.global.jwt.exception.JwtAuthenticationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ErrorMessage errorMessage;
        if (authException instanceof JwtAuthenticationException) {
            errorMessage = ErrorMessage.of(((JwtAuthenticationException) authException).getErrorCode());
        } else {
            log.trace(authException.getMessage());
            errorMessage = ErrorMessage.of(ErrorCode.INVALID_AUTHORIZATION_REQUEST);
        }
        objectMapper.writeValue(response.getOutputStream(), errorMessage);
    }
}
