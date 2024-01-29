package com.coolpeace.domain.coupon.exception;

import com.coolpeace.global.exception.ApplicationException;
import com.coolpeace.global.exception.ErrorCode;

public class InvalidCouponStateEndExposureDateException extends ApplicationException {
    public InvalidCouponStateEndExposureDateException() {
        super(ErrorCode.INVALID_COUPON_STATE_END_EXPOSURE_DATE);
    }
}
