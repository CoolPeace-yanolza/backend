package com.coolpeace.api.domain.coupon.exception;

import com.coolpeace.api.global.exception.ApplicationException;
import com.coolpeace.api.global.exception.ErrorCode;

public class CouponAccessDeniedException extends ApplicationException {
    public CouponAccessDeniedException() {
        super(ErrorCode.COUPON_ACCESS_DENIED);
    }
}
