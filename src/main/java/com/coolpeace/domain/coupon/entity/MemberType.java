package com.coolpeace.domain.coupon.entity;

import lombok.Getter;

@Getter
public enum MemberType {
    ALL_CLIENT("모든 고객"),
    FIRST_CLIENT("첫방문 고객"),
    RE_CLIENT("재방문 고객");

    private final String value;

    MemberType(String value) {
        this.value = value;
    }
}
