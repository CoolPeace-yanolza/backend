package com.coolpeace.api.domain.member.exception;

import com.coolpeace.api.global.exception.ApplicationException;
import com.coolpeace.api.global.exception.ErrorCode;

public class MemberAlreadyExistedException extends ApplicationException {
    public MemberAlreadyExistedException() {
        super(ErrorCode.MEMBER_ALREADY_EXISTED);
    }
}
