package com.coolpeace.domain.coupon.dto.request.type;

import com.coolpeace.global.common.ValuedEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SearchCouponDateFilterType implements ValuedEnum {
    YEAR("1년"),
    THREE_MONTHS("3개월"),
    SIX_MONTHS("6개월");

    private final String value;
}
