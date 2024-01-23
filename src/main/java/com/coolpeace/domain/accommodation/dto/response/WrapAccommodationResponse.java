package com.coolpeace.domain.accommodation.dto.response;

import java.util.List;

public record WrapAccommodationResponse(
    List<AccommodationResponse> accommodationResponses
) {
    public static WrapAccommodationResponse from(List<AccommodationResponse> accommodationResponses) {
        return new WrapAccommodationResponse(accommodationResponses);
    }
}
