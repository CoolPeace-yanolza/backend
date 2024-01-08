package com.coolpeace.domain.coupon.exception;

import com.coolpeace.global.exception.ApplicationException;
import com.coolpeace.global.exception.ErrorCode;

public class InvalidCouponStateInsideExposureDateException extends ApplicationException {
    public InvalidCouponStateInsideExposureDateException() {
        super(ErrorCode.INVALID_COUPON_STATE_INSIDE_EXPOSURE_DATE);
    }
}
