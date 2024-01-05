package com.coolpeace.domain.reservation.repository;

import com.coolpeace.domain.accommodation.entity.Accommodation;
import com.coolpeace.domain.reservation.entity.Reservation;
import java.util.List;

public interface ReservationRepositoryCustom {
    List<Reservation> findByAccommodation(Accommodation accommodation);
}
