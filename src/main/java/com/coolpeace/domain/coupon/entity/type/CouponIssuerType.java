package com.coolpeace.domain.coupon.entity.type;

import com.coolpeace.global.common.ValuedEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CouponIssuerType implements ValuedEnum {
    YANOLJA("YC"),
    OWNER("CC");

    private final String value;
}
