package com.coolpeace.domain.accommodation.exception;

import com.coolpeace.global.exception.ApplicationException;
import com.coolpeace.global.exception.ErrorCode;

public class AccommodationNotFoundException extends ApplicationException {
    public AccommodationNotFoundException() {
        super(ErrorCode.ACCOMMODATION_NOT_FOUND);
    }
}
