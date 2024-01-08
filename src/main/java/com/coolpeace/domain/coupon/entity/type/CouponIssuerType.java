package com.coolpeace.domain.coupon.entity.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CouponIssuerType {
    YANOLJA("YP"),
    OWNER("CP");

    private final String value;
}
