package com.coolpeace.domain.room.exception;

import com.coolpeace.global.exception.ApplicationException;
import com.coolpeace.global.exception.ErrorCode;

public class RoomNotFoundException extends ApplicationException {
    public RoomNotFoundException() {
        super(ErrorCode.ROOM_NOT_FOUND);
    }
}
