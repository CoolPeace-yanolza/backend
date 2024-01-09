package com.coolpeace.api.domain.reservation.repository;

import com.coolpeace.api.domain.accommodation.entity.Accommodation;
import com.coolpeace.api.domain.reservation.entity.Reservation;

import java.util.List;

public interface ReservationRepositoryCustom {
    List<Reservation> findByAccommodation(Accommodation accommodation);
}
