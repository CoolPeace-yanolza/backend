package com.coolpeace.domain.member.exception;

import com.coolpeace.global.exception.ApplicationException;
import com.coolpeace.global.exception.ErrorCode;

public class MemberRoleNotFoundException extends ApplicationException {
    public MemberRoleNotFoundException() {
        super(ErrorCode.MEMBER_ROLE_NOT_FOUND);
    }
}
