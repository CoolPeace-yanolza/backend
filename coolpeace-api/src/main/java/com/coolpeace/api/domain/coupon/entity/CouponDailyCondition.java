package com.coolpeace.api.domain.coupon.entity;

public enum CouponDailyCondition {
    NO_REGISTER("등록된 쿠폰이 없음"),
    NO_EXPOSURE("노출중인 쿠폰이 없음"),
    EXPIRATION_3DAYS("곧 만료되는 쿠폰이 있음"),
    NO_CONDITION("아무 조건에 해당하지 않음");

    private final String value;

    CouponDailyCondition(String value) {
        this.value = value;
    }
}
