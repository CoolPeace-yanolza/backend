package com.coolpeace.api.global.jwt.exception;

import com.coolpeace.core.exception.ErrorCode;
import lombok.Getter;

@Getter
public class JwtInvalidSignatureException extends JwtAuthenticationException {
    public JwtInvalidSignatureException() {
        super(ErrorCode.JWT_INVALID_SIGNATURE);
    }
}
