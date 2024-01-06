package com.coolpeace.domain.reservation.repository;

import com.coolpeace.domain.accommodation.entity.Accommodation;
import com.coolpeace.domain.reservation.entity.Reservation;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation,Long>, ReservationRepositoryCustom {

}
