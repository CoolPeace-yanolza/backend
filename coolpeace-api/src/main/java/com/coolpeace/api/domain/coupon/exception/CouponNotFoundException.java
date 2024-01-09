package com.coolpeace.api.domain.coupon.exception;

import com.coolpeace.api.global.exception.ErrorCode;
import com.coolpeace.api.global.exception.ApplicationException;

public class CouponNotFoundException extends ApplicationException {
    public CouponNotFoundException() {
        super(ErrorCode.COUPON_NOT_FOUND);
    }
}
