package com.coolpeace.core.domain.reservation.repository;

import com.coolpeace.core.domain.accommodation.entity.Accommodation;
import com.coolpeace.core.domain.reservation.entity.Reservation;
import com.coolpeace.core.domain.room.entity.RoomReservation;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.List;

import static com.coolpeace.core.domain.room.entity.QRoomReservation.roomReservation;

public class ReservationRepositoryImpl implements ReservationRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    public ReservationRepositoryImpl(EntityManager entityManager) {
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<Reservation> findByAccommodation(Accommodation accommodation) {

        return jpaQueryFactory.selectFrom(roomReservation)
            .where(roomReservation.room.accommodation.eq(accommodation)).fetch().stream()
            .map(RoomReservation::getReservation).toList();
    }


}