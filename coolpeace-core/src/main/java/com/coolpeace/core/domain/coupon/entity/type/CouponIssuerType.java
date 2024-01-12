package com.coolpeace.core.domain.coupon.entity.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CouponIssuerType {
    YANOLJA("YC"),
    OWNER("CC");

    private final String value;
}
