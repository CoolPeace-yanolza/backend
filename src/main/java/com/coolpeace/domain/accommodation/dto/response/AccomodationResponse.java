package com.coolpeace.domain.accommodation.dto.response;

import com.coolpeace.domain.accommodation.entity.Accommodation;
import lombok.Builder;

@Builder
public class AccomodationResponse {

    private Long id;

    private String name;

    private Integer sidoId;

    private String sido;

    private Integer sigunguId;

    private String sigungu;

    private String address;

    public static AccomodationResponse fromEntity(Accommodation accommodation){
        return AccomodationResponse.builder()
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
