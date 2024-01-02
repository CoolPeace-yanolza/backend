package com.coolpeace.global.jwt.exception;

import com.coolpeace.global.exception.ErrorCode;
import lombok.Getter;

@Getter
public class JwtInvalidSignatureException extends JwtAuthenticationException {
    public JwtInvalidSignatureException() {
        super(ErrorCode.JWT_INVALID_SIGNATURE);
    }
}
