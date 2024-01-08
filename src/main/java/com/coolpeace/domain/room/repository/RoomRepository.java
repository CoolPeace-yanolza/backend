package com.coolpeace.domain.room.repository;

import com.coolpeace.domain.room.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {
    Optional<Room> findByRoomNumber(int roomNumber);
}
