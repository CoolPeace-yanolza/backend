package com.coolpeace.domain.reservation.dto.response;

import org.springframework.data.domain.Page;

import java.util.List;

public record ReservationPageResponse(
        List<ReservationResponse> reservations,
        int totalPages,
        long totalElements,
        int size,
        int number,
        int numberOfElements,
        boolean empty,
        boolean first,
        boolean last
) {
    public static ReservationPageResponse from(Page<ReservationResponse> reservationPage) {
        
        return new ReservationPageResponse(
                reservationPage.getContent(),
                reservationPage.getTotalPages(),
                reservationPage.getTotalElements(),
                reservationPage.getSize(),
                reservationPage.getNumber(),
                reservationPage.getNumberOfElements(),
                reservationPage.isEmpty(),
                reservationPage.isFirst(),
                reservationPage.isLast()
        );
    }
}
