package com.coolpeace.domain.coupon.exception;

import com.coolpeace.global.exception.ApplicationException;
import com.coolpeace.global.exception.ErrorCode;

public class CouponAccessDeniedException extends ApplicationException {
    public CouponAccessDeniedException() {
        super(ErrorCode.COUPON_ACCESS_DENIED);
    }
}
