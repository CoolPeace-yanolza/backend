package com.coolpeace.api.global.jwt.exception;

import com.coolpeace.api.global.exception.ApplicationException;
import com.coolpeace.api.global.exception.ErrorCode;

public class JwtExpiredRefreshTokenException extends ApplicationException {
    public JwtExpiredRefreshTokenException() {
        super(ErrorCode.JWT_EXPIRED_AUTHORIZATION);
    }
}
