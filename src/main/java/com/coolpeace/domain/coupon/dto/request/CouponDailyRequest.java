package com.coolpeace.domain.coupon.dto.request;


public record CouponDailyRequest(
    Long memberId,
    Long accommodationId
) {

}
