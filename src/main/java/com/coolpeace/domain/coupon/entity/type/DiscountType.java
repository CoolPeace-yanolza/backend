package com.coolpeace.domain.coupon.entity.type;

import lombok.Getter;

@Getter
public enum DiscountType {
    FIXED_RATE("정률 할인"),
    FIXED_PRICE("정액 할인");

    private final String value;

    DiscountType(String value) {
        this.value = value;
    }
}
