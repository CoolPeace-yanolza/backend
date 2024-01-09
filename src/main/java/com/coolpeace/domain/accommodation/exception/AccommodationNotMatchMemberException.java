package com.coolpeace.domain.accommodation.exception;

import com.coolpeace.global.exception.ApplicationException;
import com.coolpeace.global.exception.ErrorCode;

public class AccommodationNotMatchMemberException extends ApplicationException {

    public AccommodationNotMatchMemberException() { super(ErrorCode.ACCOMMODATION_NOT_MATCH_MEMBER);}
}
