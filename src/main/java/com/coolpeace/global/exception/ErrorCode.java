package com.coolpeace.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // COMMON
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 에러입니다."),
    INVALID_AUTHORIZATION_REQUEST(HttpStatus.UNAUTHORIZED, "서버에서 인증 오류가 발생했습니다."),
    INVALID_AUTHORIZATION_HEADER(HttpStatus.UNAUTHORIZED, "인증 헤더가 올바르지 않습니다."),
    INVALID_REQUEST_PARAM(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),

    // MEMBER
    MEMBER_ROLE_NOT_FOUND(HttpStatus.NOT_FOUND, "회원 권한이 존재하지 않습니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 이메일의 회원 정보를 찾을 수 없습니다."),
    MEMBER_WRONG_PASSWORD(HttpStatus.NOT_FOUND, "회원 비밀번호가 올바르지 않습니다."),
    MEMBER_ALREADY_EXISTED(HttpStatus.BAD_REQUEST, "이미 회원가입이 되어 있는 이메일입니다."),

    // ACCOMMODATION
    COUPON_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 쿠폰의 정보를 찾을 수 없습니다."),

    // ROOM
    ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 숙박업체의 방의 정보를 찾을 수 없습니다."),

    // COUPON
    COUPON_ACCESS_DENIED(HttpStatus.NOT_FOUND, "해당 쿠폰에 대한 편집 권한이 없습니다."),

    // SETTLEMENT

    // RESERVATION

    //statistics
    DAILY_STATISTICS_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 일별 통계를 찾을 수 없습니다."),
    MONTHLY_STATISTICS_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 월별 통계를 찾을 수 없습니다."),

    // JWT
    JWT_INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 리프레시 토큰입니다."),
    JWT_INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 액세스 토큰입니다."),

    JWT_EXPIRED_AUTHORIZATION(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
    JWT_INVALID_SIGNATURE(HttpStatus.UNAUTHORIZED, "토큰의 시그니처가 올바르지 않습니다."),
    JWT_MALFORMED_STRUCTURE(HttpStatus.UNAUTHORIZED, "토큰의 구조가 올바르지 않습니다."),
    JWT_UNSUPPORTED_FORMAT(HttpStatus.UNAUTHORIZED, "지원하지 않는 형식의 토큰입니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
