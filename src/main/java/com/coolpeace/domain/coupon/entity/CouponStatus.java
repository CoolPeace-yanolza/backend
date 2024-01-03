package com.coolpeace.domain.coupon.entity;

public enum CouponStatus {
    EXPOSURE_ON("노출 ON"),
    EXPOSURE_OFF("노출 OFF"),
    EXPOSURE_WAIT("노출 대기중"),
    EXPOSURE_END("노출 종료"),
    DELETED("논리 삭제");

    private final String value;

    CouponStatus(String value) {
        this.value = value;
    }
}
