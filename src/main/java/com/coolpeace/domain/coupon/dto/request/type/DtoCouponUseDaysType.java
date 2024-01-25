package com.coolpeace.domain.coupon.dto.request.type;

import com.coolpeace.global.common.ValuedEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DtoCouponUseDaysType implements ValuedEnum {
    ONEDAY("하루만"),
    WEEKDAY("평일"),
    WEEKEND("주말");

    private final String value;
}
