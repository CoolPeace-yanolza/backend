package com.coolpeace.domain.reservation.entity;

import com.coolpeace.domain.reservation.entity.type.ReservationStatusType;
import com.coolpeace.domain.room.entity.RoomReservation;
import com.coolpeace.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    @Column(nullable = false)
    private int totalPrice = 0;

    @Column(nullable = false)
    private int discountPrice = 0;

    @Column(nullable = false)
    private ReservationStatusType reservationStatus = ReservationStatusType.PENDING;

    @OneToMany(mappedBy = "reservation", fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<RoomReservation> roomReservations;

    public Reservation(LocalDateTime startDate,
                       LocalDateTime endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public static Reservation from(LocalDateTime startDate,
                            LocalDateTime endDate) {
        return new Reservation(startDate, endDate);
    }

    public void updateRoomReservationAndPrices(List<RoomReservation> roomReservations, int roomTotalPrice) {
        this.roomReservations = roomReservations;
        this.discountPrice = this.roomReservations.stream().mapToInt(RoomReservation::getDiscountPrice).sum();
        this.totalPrice = roomTotalPrice - this.discountPrice;
    }

    public void updateReservation(ReservationStatusType reservationStatus) {
        this.reservationStatus = reservationStatus;
    }
}
