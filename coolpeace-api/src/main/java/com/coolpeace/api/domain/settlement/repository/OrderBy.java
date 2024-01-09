package com.coolpeace.api.domain.settlement.repository;

public enum OrderBy {
    COUPON_USE_DATE("couponUseDate"),
    SUM_PRICE("sumPrice"),
    COMPLETE_AT("completeAt"),
    COUPON_COUNT("couponCount");

    private final String value;

    OrderBy(String value) {this.value = value;}

}
