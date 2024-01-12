package com.coolpeace.api.domain.coupon.exception;

import com.coolpeace.core.exception.ApplicationException;
import com.coolpeace.core.exception.ErrorCode;

public class CouponAccessDeniedException extends ApplicationException {
    public CouponAccessDeniedException() {
        super(ErrorCode.COUPON_ACCESS_DENIED);
    }
}
