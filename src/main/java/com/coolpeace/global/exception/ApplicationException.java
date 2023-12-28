package com.coolpeace.global.exception;

import lombok.Getter;


@Getter
public class ApplicationException extends RuntimeException {

    private final ErrorCode errorCode;


    protected ApplicationException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * @param errorCode HttpResponse 에 에러 관련 메시지를 전송하기 위해 공통적으로 설정한 ErrorCode 입니다.
     * @param message 실제 로그에 적재할 Exception Message 입니다. 최대한 자세하게 작성하시면 디버깅에 편합니다.
     * @author Nine-JH
     */
    protected ApplicationException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    @Override
    public String getMessage() {
        if (super.getMessage() == null) {
            return errorCode.getMessage();
        }

        return super.getMessage();
    }
}
