package com.coolpeace.core.domain.coupon.dto.request;

import com.coolpeace.core.common.validator.ValidEnum;
import com.coolpeace.core.domain.coupon.dto.request.type.SearchCouponDateFilterType;
import com.coolpeace.core.domain.coupon.dto.request.type.SearchCouponStatusFilterType;

public record SearchCouponParams(
        @ValidEnum(enumClass = SearchCouponStatusFilterType.class, message = "올바른 쿠폰 상태 값을 입력해주세요.")
        SearchCouponStatusFilterType status,
        String title,
        @ValidEnum(enumClass = SearchCouponDateFilterType.class, message = "올바른 쿠폰 날짜 값을 입력해주세요.")
        SearchCouponDateFilterType date
) {
}
