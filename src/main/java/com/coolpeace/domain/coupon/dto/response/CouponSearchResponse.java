package com.coolpeace.domain.coupon.dto.response;

import java.util.List;

public record CouponSearchResponse() {
    public static CouponSearchResponse from(List<CouponResponse> couponResponses) {
        return new CouponSearchResponse();
    }
}
