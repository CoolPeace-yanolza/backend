package com.coolpeace.api.domain.member.exception;

import com.coolpeace.core.exception.ApplicationException;
import com.coolpeace.core.exception.ErrorCode;

public class MemberWrongPasswordException extends ApplicationException {
    public MemberWrongPasswordException() {
        super(ErrorCode.MEMBER_WRONG_PASSWORD);
    }
}
