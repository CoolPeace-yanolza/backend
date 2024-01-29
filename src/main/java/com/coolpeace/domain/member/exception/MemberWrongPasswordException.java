package com.coolpeace.domain.member.exception;

import com.coolpeace.global.exception.ApplicationException;
import com.coolpeace.global.exception.ErrorCode;

public class MemberWrongPasswordException extends ApplicationException {
    public MemberWrongPasswordException() {
        super(ErrorCode.MEMBER_WRONG_PASSWORD);
    }
}
