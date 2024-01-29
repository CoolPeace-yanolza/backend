package com.coolpeace.domain.statistics.exception;

import com.coolpeace.global.exception.ApplicationException;
import com.coolpeace.global.exception.ErrorCode;

public class LocalCouponDownloadNotFoundException extends ApplicationException {
    public LocalCouponDownloadNotFoundException(){super(ErrorCode.LOCAL_COUPON_DOWNLOAD_NOT_FOUND);}

}
