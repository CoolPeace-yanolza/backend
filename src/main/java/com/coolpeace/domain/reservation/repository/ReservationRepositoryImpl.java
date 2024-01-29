package com.coolpeace.domain.reservation.repository;

import static com.coolpeace.domain.room.entity.QRoomReservation.roomReservation;

import com.coolpeace.domain.accommodation.entity.Accommodation;
import com.coolpeace.domain.data.dto.request.SettlementStatistic;
import com.coolpeace.domain.reservation.entity.Reservation;
import com.coolpeace.domain.room.entity.RoomReservation;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class ReservationRepositoryImpl implements ReservationRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    public ReservationRepositoryImpl(EntityManager entityManager) {
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<Reservation> findByAccommodation(int year, int month, int day,Accommodation accommodation) {
        LocalDateTime startDate = LocalDateTime.of(year, month, day, 0,0);
        LocalDateTime endDate = LocalDateTime.of(year, month, day, 23,59);

        return jpaQueryFactory.selectFrom(roomReservation)
            .where(roomReservation.room.accommodation.eq(accommodation)
                .and(roomReservation.reservation.startDate
                    .between(startDate,endDate))).fetch().stream()
            .map(RoomReservation::getReservation).toList();
    }


}
