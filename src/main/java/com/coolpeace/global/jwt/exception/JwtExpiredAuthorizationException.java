package com.coolpeace.global.jwt.exception;

import com.coolpeace.global.exception.ErrorCode;
import lombok.Getter;

@Getter
public class JwtExpiredAuthorizationException extends JwtAuthenticationException {
    public JwtExpiredAuthorizationException() {
        super(ErrorCode.JWT_EXPIRED_AUTHORIZATION);
    }
}
