package com.coolpeace.domain.coupon.dto.request.type;

import com.coolpeace.global.common.ValuedEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SearchCouponStatusFilterType implements ValuedEnum {
    All("전체"),
    EXPOSURE_ON("노출 ON"),
    EXPOSURE_OFF("노출 OFF"),
    EXPIRED("만료");

    private final String value;
}
