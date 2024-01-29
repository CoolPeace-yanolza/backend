package com.coolpeace.domain.coupon.entity.type;

import com.coolpeace.global.common.ValuedEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DiscountType implements ValuedEnum {
    FIXED_RATE("정률"),
    FIXED_PRICE("정액");

    private final String value;
}
