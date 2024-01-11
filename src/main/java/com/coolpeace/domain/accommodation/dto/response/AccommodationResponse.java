package com.coolpeace.domain.accommodation.dto.response;

import com.coolpeace.domain.accommodation.entity.Accommodation;
import lombok.Builder;

@Builder
public record AccommodationResponse (

    Long id,

    String name,

    Integer sidoId,

    String sido,

    Integer sigunguId,

    String sigungu,

    String address

){
    public static AccommodationResponse fromEntity(Accommodation accommodation){
        return AccommodationResponse.builder()
            .id(accommodation.getId())
            .name(accommodation.getName())
            .sigunguId(accommodation.getSigungu().getId())
            .sigungu(accommodation.getSigungu().getName())
            .sidoId(accommodation.getSido().getId())
            .sido(accommodation.getSido().getName())
            .address(accommodation.getAddress())
            .build();
    }

}
