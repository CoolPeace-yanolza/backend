package com.coolpeace.domain.coupon.exception;

import com.coolpeace.global.exception.ApplicationException;
import com.coolpeace.global.exception.ErrorCode;

public class CouponUpdateLimitExposureStateException extends ApplicationException {
    public CouponUpdateLimitExposureStateException() {
        super(ErrorCode.COUPON_UPDATE_LIMIT_EXPOSURE_STATE);
    }
}
