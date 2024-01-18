package com.coolpeace.domain.room.entity;

import com.coolpeace.domain.coupon.entity.Coupon;
import com.coolpeace.domain.reservation.entity.Reservation;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoomReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int discountPrice = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    @ManyToOne
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    public RoomReservation(Room room, Reservation reservation) {
        this.room = room;
        this.reservation = reservation;
    }

    public RoomReservation(Room room, Reservation reservation, Coupon coupon) {
        this.room = room;
        this.reservation = reservation;
        this.coupon = coupon;
        this.discountPrice = switch (coupon.getDiscountType()) {
            case FIXED_RATE -> room.getPrice() * coupon.getDiscountValue();
            case FIXED_PRICE -> coupon.getDiscountValue();
        };
    }

    public static RoomReservation from(Room room, Reservation reservation, Coupon coupon) {
        return new RoomReservation(room, reservation, coupon);
    }
}
