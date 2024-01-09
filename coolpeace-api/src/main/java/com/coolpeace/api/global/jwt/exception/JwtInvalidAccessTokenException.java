package com.coolpeace.api.global.jwt.exception;

import com.coolpeace.api.global.exception.ErrorCode;

public class JwtInvalidAccessTokenException extends JwtAuthenticationException {
    public JwtInvalidAccessTokenException() {
        super(ErrorCode.JWT_INVALID_ACCESS_TOKEN);
    }
}
