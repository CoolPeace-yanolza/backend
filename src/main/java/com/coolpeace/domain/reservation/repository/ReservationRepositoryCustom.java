package com.coolpeace.domain.reservation.repository;

import com.coolpeace.domain.accommodation.entity.Accommodation;
import com.coolpeace.domain.data.dto.request.SettlementQueryDto;
import com.coolpeace.domain.reservation.entity.Reservation;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.Query;

public interface ReservationRepositoryCustom {
    List<Reservation> findByAccommodation(int year, int month, int day, Accommodation accommodation);

    @Query(value = "select r.* , rr.coupon_id from Reservation r , RoomReservation rr "
        + "where rr.reservation_id = r.id "
        + "and  DATE_FORMAT(r.startDate, '%Y-%m-%d') = :date", nativeQuery = true)
    List<SettlementQueryDto> findByDate(LocalDate date);
}
