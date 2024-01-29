package com.coolpeace.domain.room.repository;

import com.coolpeace.domain.accommodation.entity.Accommodation;
import com.coolpeace.domain.room.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;

public interface RoomRepository extends JpaRepository<Room, Long> {
    Optional<Room> findByRoomNumber(String roomNumber);

    List<Room> findAllByAccommodation(Accommodation accommodation);

    @Query(value = "select r.* from room r, coupon_rooms cr "
        + "where r.id = cr.room_id "
        + "order by RAND() "
        + "limit 1 " , nativeQuery = true)
    Room findRandom();
}
