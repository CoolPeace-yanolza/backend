package com.coolpeace.domain.coupon.exception;

import com.coolpeace.global.exception.ApplicationException;
import com.coolpeace.global.exception.ErrorCode;

public class InvalidCouponStateOutsideExposureDateException extends ApplicationException {
    public InvalidCouponStateOutsideExposureDateException() {
        super(ErrorCode.INVALID_COUPON_STATE_OUTSIDE_EXPOSURE_DATE);
    }
}
