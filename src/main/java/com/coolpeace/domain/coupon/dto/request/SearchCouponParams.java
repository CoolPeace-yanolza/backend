package com.coolpeace.domain.coupon.dto.request;

import com.coolpeace.domain.coupon.dto.request.type.SearchCouponDateFilterType;
import com.coolpeace.domain.coupon.dto.request.type.SearchCouponStatusFilterType;
import com.coolpeace.global.common.validator.ValidEnum;

public record SearchCouponParams(
        @ValidEnum(enumClass = SearchCouponStatusFilterType.class, message = "올바른 쿠폰 상태 값을 입력해주세요.")
        String status,
        String title,
        @ValidEnum(enumClass = SearchCouponDateFilterType.class, message = "올바른 쿠폰 날짜 값을 입력해주세요.")
        String date
) {
}
