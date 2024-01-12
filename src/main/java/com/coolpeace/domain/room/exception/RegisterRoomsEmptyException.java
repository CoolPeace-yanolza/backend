package com.coolpeace.domain.room.exception;

import com.coolpeace.global.exception.ApplicationException;
import com.coolpeace.global.exception.ErrorCode;

public class RegisterRoomsEmptyException extends ApplicationException {
    public RegisterRoomsEmptyException() {
        super(ErrorCode.REGISTER_ROOMS_EMPTY);
    }
}
