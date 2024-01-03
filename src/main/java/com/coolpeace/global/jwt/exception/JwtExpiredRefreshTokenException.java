package com.coolpeace.global.jwt.exception;

import com.coolpeace.global.exception.ApplicationException;
import com.coolpeace.global.exception.ErrorCode;

public class JwtExpiredRefreshTokenException extends ApplicationException {
    public JwtExpiredRefreshTokenException() {
        super(ErrorCode.JWT_EXPIRED_AUTHORIZATION);
    }
}
