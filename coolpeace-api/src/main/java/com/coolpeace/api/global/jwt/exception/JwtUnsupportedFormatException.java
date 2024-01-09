package com.coolpeace.api.global.jwt.exception;

import com.coolpeace.api.global.exception.ErrorCode;
import lombok.Getter;

@Getter
public class JwtUnsupportedFormatException extends JwtAuthenticationException {
    public JwtUnsupportedFormatException() {
        super(ErrorCode.JWT_UNSUPPORTED_FORMAT);
    }
}
