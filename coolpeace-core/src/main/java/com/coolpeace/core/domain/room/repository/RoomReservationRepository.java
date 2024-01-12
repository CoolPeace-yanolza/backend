package com.coolpeace.core.domain.room.repository;

import com.coolpeace.core.domain.room.entity.RoomReservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomReservationRepository extends JpaRepository<RoomReservation,Long> {

}
