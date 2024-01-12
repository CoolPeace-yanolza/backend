package com.coolpeace.api.domain.coupon.dto.request;

import com.coolpeace.core.common.validator.ValidEnum;
import com.coolpeace.core.domain.coupon.entity.type.CouponStatusType;
import jakarta.validation.constraints.NotNull;

public record CouponExposeRequest(

        @NotNull(message = "쿠폰의 상태를 입력해야 합니다.")
        @ValidEnum(enumClass = CouponStatusType.class, message = "올바른 쿠폰 상태를 입력해야 합니다.")
        CouponStatusType couponStatus
) {
}
