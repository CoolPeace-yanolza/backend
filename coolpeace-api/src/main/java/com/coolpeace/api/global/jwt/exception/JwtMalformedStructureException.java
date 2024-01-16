package com.coolpeace.api.global.jwt.exception;

import com.coolpeace.core.exception.ErrorCode;
import lombok.Getter;

@Getter
public class JwtMalformedStructureException extends JwtAuthenticationException {
    public JwtMalformedStructureException() {
        super(ErrorCode.JWT_MALFORMED_STRUCTURE);
    }
}