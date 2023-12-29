package com.coolpeace.global.exception;

public record ErrorMessage (
        String code,
        String message
) {
    public static ErrorMessage of(ErrorCode errorCode) {
        return new ErrorMessage(errorCode.name(), errorCode.getMessage());
    }

    public static ErrorMessage of(ErrorCode errorCode, String errorMessage) {
        return new ErrorMessage(errorCode.name(), errorMessage);
    }
}
