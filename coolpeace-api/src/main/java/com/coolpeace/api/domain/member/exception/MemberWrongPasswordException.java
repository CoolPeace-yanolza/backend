package com.coolpeace.api.domain.member.exception;

import com.coolpeace.api.global.exception.ApplicationException;
import com.coolpeace.api.global.exception.ErrorCode;

public class MemberWrongPasswordException extends ApplicationException {
    public MemberWrongPasswordException() {
        super(ErrorCode.MEMBER_WRONG_PASSWORD);
    }
}
