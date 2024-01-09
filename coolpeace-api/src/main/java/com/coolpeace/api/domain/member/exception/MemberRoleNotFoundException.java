package com.coolpeace.api.domain.member.exception;

import com.coolpeace.api.global.exception.ApplicationException;
import com.coolpeace.api.global.exception.ErrorCode;

public class MemberRoleNotFoundException extends ApplicationException {
    public MemberRoleNotFoundException() {
        super(ErrorCode.MEMBER_ROLE_NOT_FOUND);
    }
}
