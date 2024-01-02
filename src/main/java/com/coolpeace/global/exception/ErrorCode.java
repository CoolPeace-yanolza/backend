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
    MEMBER_ROLE_NOT_FOUND(HttpStatus.NOT_FOUND, "회원 권한이 존재하지 않습니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "회원 아이디가 존재하지 않습니다."),
    MEMBER_WRONG_PASSWORD(HttpStatus.NOT_FOUND, "회원 비밀번호가 올바르지 입니다."),
    MEMBER_ALREADY_EXISTED(HttpStatus.BAD_REQUEST, "회원가입이 되어 있는 이메일입니다.")

    // ACCOMMODATION

    // ROOM

    // COUPON

    // SETTLEMENT

    // RESERVATION

    ;
    private final HttpStatus httpStatus;
    private final String message;
}
