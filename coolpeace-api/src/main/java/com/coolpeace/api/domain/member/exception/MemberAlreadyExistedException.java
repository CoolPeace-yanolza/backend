package com.coolpeace.api.domain.member.exception;

import com.coolpeace.core.exception.ApplicationException;
import com.coolpeace.core.exception.ErrorCode;

public class MemberAlreadyExistedException extends ApplicationException {
    public MemberAlreadyExistedException() {
        super(ErrorCode.MEMBER_ALREADY_EXISTED);
    }
}
