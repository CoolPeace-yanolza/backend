package com.coolpeace.domain.reservation.repository;

import com.coolpeace.domain.accommodation.entity.Accommodation;
import com.coolpeace.domain.data.dto.request.SettlementStatistic;
import com.coolpeace.domain.reservation.entity.Reservation;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.Query;

public interface ReservationRepositoryCustom {
    List<Reservation> findByAccommodation(int year, int month, int day, Accommodation accommodation);


}
