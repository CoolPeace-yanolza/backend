package com.coolpeace.domain.coupon.dto.response;

import java.util.List;
import lombok.Builder;

@Builder
public record CouponDailyResponse(
    String condition,
    List<String> couponTitle
) {
}
