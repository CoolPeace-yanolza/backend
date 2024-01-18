package com.coolpeace.domain.coupon.entity.type;

import com.coolpeace.global.common.ValuedEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CouponStatusType implements ValuedEnum {
    EXPOSURE_ON("노출 ON"),
    EXPOSURE_OFF("노출 OFF"),
    EXPOSURE_WAIT("노출 대기중"),
    EXPOSURE_END("노출 기간 만료"),
    DELETED("삭제");

    private final String value;
}
