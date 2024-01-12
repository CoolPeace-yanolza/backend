package com.coolpeace.api.domain.coupon.exception;

import com.coolpeace.core.exception.ApplicationException;
import com.coolpeace.core.exception.ErrorCode;

public class InvalidCouponStateInsideExposureDateException extends ApplicationException {
    public InvalidCouponStateInsideExposureDateException() {
        super(ErrorCode.INVALID_COUPON_STATE_INSIDE_EXPOSURE_DATE);
    }
}
