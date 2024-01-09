package com.coolpeace.api.domain.accommodation.exception;

import com.coolpeace.api.global.exception.ApplicationException;
import com.coolpeace.api.global.exception.ErrorCode;

public class AccommodationNotFoundException extends ApplicationException {

    public AccommodationNotFoundException() {
        super(ErrorCode.ACCOMMODATION_NOT_FOUND);
    }

}
