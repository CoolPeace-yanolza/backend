package com.coolpeace.domain.coupon.dto.response;

public record CouponCategoryResponse (
        Long all,
        Long exposureOn,
        Long exposureOff,
        Long expiration
) {
    public static CouponCategoryResponse from(Long all, Long exposureOn, Long exposureOff, Long expiration) {
        return new CouponCategoryResponse(all, exposureOn, exposureOff, expiration);
    }
}
