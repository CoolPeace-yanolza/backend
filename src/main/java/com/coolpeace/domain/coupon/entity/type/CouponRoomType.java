package com.coolpeace.domain.coupon.entity.type;

import com.coolpeace.global.common.ValuedEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CouponRoomType implements ValuedEnum {
    RENTAL("대실"),
    LODGE("숙박"),
    TWO_NIGHT("2박 이상");

    private final String value;
}
