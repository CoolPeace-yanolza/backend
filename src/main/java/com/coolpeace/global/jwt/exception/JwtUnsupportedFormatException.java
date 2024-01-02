package com.coolpeace.global.jwt.exception;

import com.coolpeace.global.exception.ErrorCode;
import lombok.Getter;

@Getter
public class JwtUnsupportedFormatException extends JwtAuthenticationException {
    public JwtUnsupportedFormatException() {
        super(ErrorCode.JWT_UNSUPPORTED_FORMAT);
    }
}
