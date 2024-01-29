package com.coolpeace.domain.room.entity;

import com.coolpeace.domain.accommodation.entity.Accommodation;
import com.coolpeace.domain.coupon.entity.CouponRooms;
import com.coolpeace.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Room extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roomNumber;

    private String roomType;

    private int price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accommodation_id")
    private Accommodation accommodation;

    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private final List<RoomReservation> roomReservations = new ArrayList<>();

    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private final List<CouponRooms> couponRooms = new ArrayList<>();

    public Room(String roomNumber, String roomType, int price, Accommodation accommodation) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.price = price;
        this.accommodation = accommodation;
    }

    public static Room from(String roomNumber, String roomType, int price, Accommodation accommodation) {
        return new Room(roomNumber, roomType, price, accommodation);
    }
}
