package com.coolpeace.api.domain.room.repository;

import com.coolpeace.api.domain.accommodation.entity.Accommodation;
import com.coolpeace.api.domain.room.entity.Room;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {
    Optional<Room> findByRoomNumber(int roomNumber);

    List<Room> findAllByAccommodation(Accommodation accommodation);
}
