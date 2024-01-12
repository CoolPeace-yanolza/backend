package com.coolpeace.core.domain.coupon.entity.type;

import lombok.Getter;

@Getter
public enum DiscountType {
    FIXED_RATE("정률"),
    FIXED_PRICE("정액");

    private final String value;

    DiscountType(String value) {
        this.value = value;
    }
}
