package com.coolpeace.api.domain.room.exception;

import com.coolpeace.core.exception.ApplicationException;
import com.coolpeace.core.exception.ErrorCode;

public class RoomNotFoundException extends ApplicationException {
    public RoomNotFoundException() {
        super(ErrorCode.ROOM_NOT_FOUND);
    }
}
