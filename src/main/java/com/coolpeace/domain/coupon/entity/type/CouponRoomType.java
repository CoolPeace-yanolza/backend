package com.coolpeace.domain.coupon.entity.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CouponRoomType {
    RENTAL(0, "대실"),
    LODGE(1, "숙박"),
    TWO_NIGHT(2, "2박");

    private final Integer index;
    private final String value;
}
