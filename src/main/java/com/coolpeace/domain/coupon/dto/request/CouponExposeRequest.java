package com.coolpeace.domain.coupon.dto.request;

import com.coolpeace.domain.coupon.entity.type.CouponStatusType;
import com.coolpeace.global.common.validator.ValidEnum;

public record CouponExposeRequest(
        @ValidEnum(enumClass = CouponStatusType.class, message = "올바른 쿠폰 상태를 입력해야 합니다.")
        CouponStatusType couponStatus
) {
}
