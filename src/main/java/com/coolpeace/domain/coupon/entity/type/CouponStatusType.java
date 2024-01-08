package com.coolpeace.domain.coupon.entity.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CouponStatusType {
    EXPOSURE_ON("ON"),
    EXPOSURE_OFF("OFF"),
    EXPOSURE_WAIT("대기중"),
    EXPOSURE_END("종료"),
    DELETED("삭제");

    private final String value;
}
