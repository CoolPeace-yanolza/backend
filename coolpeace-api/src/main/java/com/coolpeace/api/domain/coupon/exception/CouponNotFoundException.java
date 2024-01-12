package com.coolpeace.api.domain.coupon.exception;

import com.coolpeace.core.exception.ErrorCode;
import com.coolpeace.core.exception.ApplicationException;

public class CouponNotFoundException extends ApplicationException {
    public CouponNotFoundException() {
        super(ErrorCode.COUPON_NOT_FOUND);
    }
}
