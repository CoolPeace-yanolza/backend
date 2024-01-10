package com.coolpeace.domain.coupon.exception;

import com.coolpeace.global.exception.ApplicationException;
import com.coolpeace.global.exception.ErrorCode;

public class InvalidCouponStateWaitExposureDateException extends ApplicationException {
    public InvalidCouponStateWaitExposureDateException() {
        super(ErrorCode.INVALID_COUPON_STATE_WAIT_EXPOSURE_DATE);
    }
}
