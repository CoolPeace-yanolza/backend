package com.coolpeace.api.domain.room.exception;

import com.coolpeace.core.exception.ApplicationException;
import com.coolpeace.core.exception.ErrorCode;

public class RegisterRoomsEmptyException extends ApplicationException {
    public RegisterRoomsEmptyException() {
        super(ErrorCode.REGISTER_ROOMS_EMPTY);
    }
}
