package com.coolpeace.api.domain.accommodation.exception;

import com.coolpeace.api.global.exception.ApplicationException;
import com.coolpeace.api.global.exception.ErrorCode;

public class AccommodationNotMatchMemberException extends ApplicationException {

    public AccommodationNotMatchMemberException() { super(ErrorCode.ACCOMMODATION_NOT_MATCH_MEMBER);}
}

