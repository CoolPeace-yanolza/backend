package com.coolpeace.api.domain.coupon.dto.request;

import com.coolpeace.api.domain.coupon.dto.request.type.SearchCouponDateFilterType;
import com.coolpeace.api.domain.coupon.dto.request.type.SearchCouponStatusFilterType;

public record SearchCouponParams(
        SearchCouponStatusFilterType status,
        String title,
        SearchCouponDateFilterType date
) {
}
