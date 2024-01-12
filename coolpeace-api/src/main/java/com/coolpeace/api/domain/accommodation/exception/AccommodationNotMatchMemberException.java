package com.coolpeace.api.domain.accommodation.exception;

import com.coolpeace.core.exception.ApplicationException;
import com.coolpeace.core.exception.ErrorCode;

public class AccommodationNotMatchMemberException extends ApplicationException {

    public AccommodationNotMatchMemberException() { super(ErrorCode.ACCOMMODATION_NOT_MATCH_MEMBER);}
}

