package com.coolpeace.core.domain.coupon.dto.request;

import com.coolpeace.core.domain.coupon.dto.request.type.SearchCouponDateFilterType;
import com.coolpeace.core.domain.coupon.dto.request.type.SearchCouponStatusFilterType;

public record SearchCouponParams(
        SearchCouponStatusFilterType status,
        String title,
        SearchCouponDateFilterType date
) {
}
