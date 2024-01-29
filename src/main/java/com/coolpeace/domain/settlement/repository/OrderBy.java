package com.coolpeace.domain.settlement.repository;

public enum OrderBy {
    COUPON_USE_DATE("쿠폰 사용일"),
    SUM_PRICE("합산 정산 금액"),
    COMPLETE_AT("정산 완료일"),
    COUPON_COUNT("사용 건수");

    private final String value;

    OrderBy(String value) {this.value = value;}

}
