package com.coolpeace.domain.coupon.dto.response;

import com.coolpeace.domain.coupon.entity.CouponDailyCondition;
import java.util.Collections;
import java.util.List;
import lombok.Builder;

@Builder
public record CouponDailyResponse(
    int conditionNum,
    CouponDailyCondition condition,
    List<String> couponTitles
) {
    public static CouponDailyResponse from(int conditionNum, CouponDailyCondition condition,List<String> couponTitles) {
        return new CouponDailyResponse(conditionNum, condition, couponTitles);
    }
    public static CouponDailyResponse from(int conditionNum, CouponDailyCondition condition) {
        return new CouponDailyResponse(conditionNum, condition, Collections.emptyList());
    }
}
