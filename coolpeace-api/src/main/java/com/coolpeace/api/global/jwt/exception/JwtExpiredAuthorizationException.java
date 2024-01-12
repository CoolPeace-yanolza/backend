package com.coolpeace.api.global.jwt.exception;

import com.coolpeace.core.exception.ErrorCode;
import lombok.Getter;

@Getter
public class JwtExpiredAuthorizationException extends JwtAuthenticationException {
    public JwtExpiredAuthorizationException() {
        super(ErrorCode.JWT_EXPIRED_AUTHORIZATION);
    }
}
