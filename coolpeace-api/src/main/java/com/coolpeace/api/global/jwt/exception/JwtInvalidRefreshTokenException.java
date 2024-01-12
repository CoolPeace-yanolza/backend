package com.coolpeace.api.global.jwt.exception;

import com.coolpeace.core.exception.ApplicationException;
import com.coolpeace.core.exception.ErrorCode;

public class JwtInvalidRefreshTokenException extends ApplicationException {
    public JwtInvalidRefreshTokenException() {
        super(ErrorCode.JWT_INVALID_REFRESH_TOKEN);
    }
}
