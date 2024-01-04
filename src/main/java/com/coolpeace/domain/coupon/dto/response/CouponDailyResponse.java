package com.coolpeace.domain.coupon.dto.response;

import com.coolpeace.domain.coupon.entity.CouponDailyCondition;
import java.util.Collections;
import java.util.List;
import lombok.Builder;

@Builder
public record CouponDailyResponse(
    CouponDailyCondition condition,
    List<String> couponTitles
) {
    public static CouponDailyResponse from(CouponDailyCondition condition,List<String> couponTitles) {
        return new CouponDailyResponse(condition, couponTitles);
    }
    public static CouponDailyResponse from(CouponDailyCondition condition) {
        return new CouponDailyResponse(condition, Collections.emptyList());
    }
}
