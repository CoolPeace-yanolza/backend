package com.coolpeace.api.domain.coupon.exception;

import com.coolpeace.core.exception.ApplicationException;
import com.coolpeace.core.exception.ErrorCode;

public class InvalidCouponStateEndExposureDateException extends ApplicationException {
    public InvalidCouponStateEndExposureDateException() {
        super(ErrorCode.INVALID_COUPON_STATE_END_EXPOSURE_DATE);
    }
}
