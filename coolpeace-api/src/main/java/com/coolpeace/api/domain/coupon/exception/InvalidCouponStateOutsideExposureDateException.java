package com.coolpeace.api.domain.coupon.exception;

import com.coolpeace.core.exception.ApplicationException;
import com.coolpeace.core.exception.ErrorCode;

public class InvalidCouponStateOutsideExposureDateException extends ApplicationException {
    public InvalidCouponStateOutsideExposureDateException() {
        super(ErrorCode.INVALID_COUPON_STATE_OUTSIDE_EXPOSURE_DATE);
    }
}
