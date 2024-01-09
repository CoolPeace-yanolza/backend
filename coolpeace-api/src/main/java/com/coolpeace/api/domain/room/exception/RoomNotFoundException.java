package com.coolpeace.api.domain.room.exception;

import com.coolpeace.api.global.exception.ApplicationException;
import com.coolpeace.api.global.exception.ErrorCode;

public class RoomNotFoundException extends ApplicationException {
    public RoomNotFoundException() {
        super(ErrorCode.ROOM_NOT_FOUND);
    }
}
