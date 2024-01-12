package com.coolpeace.api.global.jwt.exception;

import com.coolpeace.core.exception.ApplicationException;
import com.coolpeace.core.exception.ErrorCode;

public class JwtExpiredRefreshTokenException extends ApplicationException {
    public JwtExpiredRefreshTokenException() {
        super(ErrorCode.JWT_EXPIRED_AUTHORIZATION);
    }
}
