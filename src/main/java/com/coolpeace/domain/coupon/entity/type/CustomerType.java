package com.coolpeace.domain.coupon.entity.type;

import com.coolpeace.global.common.ValuedEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CustomerType implements ValuedEnum {
    ALL_CLIENT("모든 고객"),
    FIRST_CLIENT("첫방문 고객"),
    RE_CLIENT("재방문 고객");

    private final String value;
}
