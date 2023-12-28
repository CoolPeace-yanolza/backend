package com.coolpeace.domain.coupon.entity;

import lombok.Getter;

@Getter
public enum UseCase {
    PAY("최소 결제 금액"),
    DAYS("사용 가능 요일");

    private final String value;

    UseCase(String value) {this.value = value;}
}
