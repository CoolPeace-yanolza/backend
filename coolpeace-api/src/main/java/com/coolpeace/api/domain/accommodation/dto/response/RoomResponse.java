package com.coolpeace.api.domain.accommodation.dto.response;

import com.coolpeace.api.domain.room.entity.Room;
import lombok.Builder;

@Builder
public class RoomResponse {

    private Long id;

    private Integer roomNumber;

    private String roomType;

    private Integer price;

    public static RoomResponse fromEntity(Room room) {
        return RoomResponse.builder()
            .id(room.getId())
            .roomNumber(room.getRoomNumber())
            .roomType(room.getRoomType())
            .price(room.getPrice())
            .build();
    }
}
