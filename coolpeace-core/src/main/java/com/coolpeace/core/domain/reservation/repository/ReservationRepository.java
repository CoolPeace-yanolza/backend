package com.coolpeace.core.domain.reservation.repository;

import com.coolpeace.core.domain.reservation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation,Long>, ReservationRepositoryCustom {

}
