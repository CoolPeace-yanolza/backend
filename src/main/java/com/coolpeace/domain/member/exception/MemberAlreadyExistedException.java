package com.coolpeace.domain.member.exception;

import com.coolpeace.global.exception.ApplicationException;
import com.coolpeace.global.exception.ErrorCode;

public class MemberAlreadyExistedException extends ApplicationException {
    public MemberAlreadyExistedException() {
        super(ErrorCode.MEMBER_ALREADY_EXISTED);
    }
}
