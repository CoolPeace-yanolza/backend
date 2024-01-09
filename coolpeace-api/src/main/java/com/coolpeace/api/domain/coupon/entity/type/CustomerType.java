package com.coolpeace.api.domain.coupon.entity.type;

import lombok.Getter;

@Getter
public enum CustomerType {
    ALL_CLIENT("모든 고객"),
    FIRST_CLIENT("첫방문 고객"),
    RE_CLIENT("재방문 고객");

    private final String value;

    CustomerType(String value) {
        this.value = value;
    }
}
