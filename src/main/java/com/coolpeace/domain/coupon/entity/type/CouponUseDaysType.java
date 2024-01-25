package com.coolpeace.domain.coupon.entity.type;

import com.coolpeace.global.common.ValuedEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CouponUseDaysType implements ValuedEnum {
    MONDAY("월"),
    TUESDAY("화"),
    WEDNESDAY("수"),
    THURSDAY("목"),
    FRIDAY("금"),
    SATURDAY("토"),
    SUNDAY("일"),
    WEEKDAY("평일"),
    WEEKEND("주말");

    private final String value;

    public boolean isOneDay() {
        return !this.equals(WEEKDAY) && !this.equals(WEEKEND);
    }
}
