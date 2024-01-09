package com.coolpeace.api.global.exception;

public record ErrorMessage (
        String code,
        String message
) {
    public static ErrorMessage from(ErrorCode errorCode) {
        return new ErrorMessage(errorCode.name(), errorCode.getMessage());
    }

    public static ErrorMessage from(ErrorCode errorCode, String errorMessage) {
        return new ErrorMessage(errorCode.name(), errorMessage);
    }
}
