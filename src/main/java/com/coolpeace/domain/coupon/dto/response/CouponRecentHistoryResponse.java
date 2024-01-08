package com.coolpeace.domain.coupon.dto.response;

import com.coolpeace.domain.coupon.entity.Coupon;

public record CouponRecentHistoryResponse() {
    public static CouponRecentHistoryResponse from(Coupon recentHistory) {
        return new CouponRecentHistoryResponse();
    }
}
