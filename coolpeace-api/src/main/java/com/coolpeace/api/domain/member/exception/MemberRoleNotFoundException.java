package com.coolpeace.api.domain.member.exception;

import com.coolpeace.core.exception.ApplicationException;
import com.coolpeace.core.exception.ErrorCode;

public class MemberRoleNotFoundException extends ApplicationException {
    public MemberRoleNotFoundException() {
        super(ErrorCode.MEMBER_ROLE_NOT_FOUND);
    }
}
