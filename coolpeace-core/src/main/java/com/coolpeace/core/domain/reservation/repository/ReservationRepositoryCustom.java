package com.coolpeace.core.domain.reservation.repository;

import com.coolpeace.core.domain.accommodation.entity.Accommodation;
import com.coolpeace.core.domain.reservation.entity.Reservation;

import java.util.List;

public interface ReservationRepositoryCustom {
    List<Reservation> findByAccommodation(Accommodation accommodation);
}
