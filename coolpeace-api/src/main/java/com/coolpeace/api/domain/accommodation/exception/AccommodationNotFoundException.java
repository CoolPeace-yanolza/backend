package com.coolpeace.api.domain.accommodation.exception;

import com.coolpeace.core.exception.ApplicationException;
import com.coolpeace.core.exception.ErrorCode;

public class AccommodationNotFoundException extends ApplicationException {

    public AccommodationNotFoundException() {
        super(ErrorCode.ACCOMMODATION_NOT_FOUND);
    }

}
