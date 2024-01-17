package com.coolpeace.domain.reservation.dto.response;

import com.coolpeace.domain.reservation.entity.Reservation;
import com.coolpeace.domain.reservation.entity.type.ReservationStatusType;

import java.time.LocalDateTime;

public record ReservationResponse (
        Long id,
        LocalDateTime startDate,
        LocalDateTime endDate,
        int totalPrice,
        int discountPrice,
        ReservationStatusType reservationStatus
) {
    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getStartDate(),
                reservation.getEndDate(),
                reservation.getTotalPrice(),
                reservation.getDiscountPrice(),
                reservation.getReservationStatus()
        );
    }
}
