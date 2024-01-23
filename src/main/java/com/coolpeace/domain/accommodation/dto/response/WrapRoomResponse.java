package com.coolpeace.domain.accommodation.dto.response;

import java.util.List;

public record WrapRoomResponse(
    List<RoomResponse> roomResponses
) {
    public static WrapRoomResponse from(List<RoomResponse> roomResponses) {
        return new WrapRoomResponse(roomResponses);
    }
}
