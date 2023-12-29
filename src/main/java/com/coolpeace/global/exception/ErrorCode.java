package com.coolpeace.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // COMMON
    INVALID_REQUEST_PARAM(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),

    // MEMBER
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다.")

    // ACCOMMODATION

    // ROOM

    // COUPON

    // SETTLEMENT

    // RESERVATION

    ;
    private final HttpStatus httpStatus;
    private final String message;
}
