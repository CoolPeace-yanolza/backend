package com.coolpeace.domain.accommodation.dto.response;

import com.coolpeace.domain.room.entity.Room;
import lombok.Builder;

@Builder
public record RoomResponse(

    Long id,

    Integer roomNumber,

    String roomType,

    Integer price
)
{
    public static RoomResponse fromEntity(Room room) {
        return RoomResponse.builder()
            .id(room.getId())
            .roomNumber(room.getRoomNumber())
            .roomType(room.getRoomType())
            .price(room.getPrice())
            .build();
    }
}
