package com.coolpeace.api.domain.coupon.exception;

import com.coolpeace.core.exception.ApplicationException;
import com.coolpeace.core.exception.ErrorCode;

public class InvalidCouponStateWaitExposureDateException extends ApplicationException {
    public InvalidCouponStateWaitExposureDateException() {
        super(ErrorCode.INVALID_COUPON_STATE_WAIT_EXPOSURE_DATE);
    }
}
