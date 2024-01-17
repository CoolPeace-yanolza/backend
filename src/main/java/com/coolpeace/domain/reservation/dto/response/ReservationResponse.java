package com.coolpeace.domain.reservation.dto.response;

import com.coolpeace.domain.reservation.entity.Reservation;
import org.springframework.data.domain.Page;

public record ReservationResponse (

) {
    public static ReservationResponse from(Page<Reservation> reservationPage) {
        return new ReservationResponse();
    }
}
