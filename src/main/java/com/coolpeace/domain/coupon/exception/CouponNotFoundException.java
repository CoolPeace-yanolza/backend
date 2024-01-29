package com.coolpeace.domain.coupon.exception;

import com.coolpeace.global.exception.ApplicationException;
import com.coolpeace.global.exception.ErrorCode;

public class CouponNotFoundException extends ApplicationException {
    public CouponNotFoundException() {
        super(ErrorCode.COUPON_NOT_FOUND);
    }
}
