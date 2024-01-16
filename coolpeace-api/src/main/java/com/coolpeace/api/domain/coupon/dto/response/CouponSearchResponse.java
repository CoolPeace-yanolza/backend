package com.coolpeace.api.domain.coupon.dto.response;

import org.springframework.data.domain.Page;

import java.util.List;

public record CouponSearchResponse (
        List<CouponResponse> content,
        CouponCategoryResponse category,
        int totalPages,
        long totalElements,
        int size,
        int number,
        int numberOfElements,
        boolean empty,
        boolean first,
        boolean last
) {
    public static CouponSearchResponse from(Page<CouponResponse> couponResponses, CouponCategoryResponse categoryResponse) {
        return new CouponSearchResponse(
                couponResponses.getContent(),
                categoryResponse,
                couponResponses.getTotalPages(),
                couponResponses.getTotalElements(),
                couponResponses.getSize(),
                couponResponses.getNumber(),
                couponResponses.getNumberOfElements(),
                couponResponses.isEmpty(),
                couponResponses.isFirst(),
                couponResponses.isLast()
        );
    }
}
