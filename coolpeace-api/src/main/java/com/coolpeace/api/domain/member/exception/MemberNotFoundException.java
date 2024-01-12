package com.coolpeace.api.domain.member.exception;

import com.coolpeace.core.exception.ApplicationException;
import com.coolpeace.core.exception.ErrorCode;

public class MemberNotFoundException extends ApplicationException {
    public MemberNotFoundException() {
        super(ErrorCode.MEMBER_NOT_FOUND);
    }
}
