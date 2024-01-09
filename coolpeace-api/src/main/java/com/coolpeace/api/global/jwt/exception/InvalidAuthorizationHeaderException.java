package com.coolpeace.api.global.jwt.exception;

import com.coolpeace.api.global.exception.ErrorCode;
import lombok.Getter;

@Getter
public class InvalidAuthorizationHeaderException extends JwtAuthenticationException {
    public InvalidAuthorizationHeaderException() {
        super(ErrorCode.INVALID_AUTHORIZATION_HEADER);
    }
}
