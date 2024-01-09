package com.coolpeace.api.domain.room.repository;

import com.coolpeace.api.domain.room.entity.RoomReservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomReservationRepository extends JpaRepository<RoomReservation,Long> {

}
