package com.coolpeace.global.jwt.exception;

import com.coolpeace.global.exception.ErrorCode;

public class JwtInvalidAccessTokenException extends JwtAuthenticationException {
    public JwtInvalidAccessTokenException() {
        super(ErrorCode.JWT_INVALID_ACCESS_TOKEN);
    }
}
