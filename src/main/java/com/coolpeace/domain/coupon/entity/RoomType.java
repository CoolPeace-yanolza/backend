package com.coolpeace.domain.coupon.entity;

import lombok.Getter;

@Getter
public enum RoomType {
    RENTAL("대실"),
    LODGE("숙박");


    private final String value;

    RoomType(String value) {
        this.value = value;
    }
}
