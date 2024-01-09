package com.coolpeace.api.global.jwt.exception;

import com.coolpeace.api.global.exception.ApplicationException;
import com.coolpeace.api.global.exception.ErrorCode;

public class JwtInvalidRefreshTokenException extends ApplicationException {
    public JwtInvalidRefreshTokenException() {
        super(ErrorCode.JWT_INVALID_REFRESH_TOKEN);
    }
}
