package com.coolpeace.core.domain.room.entity;

import com.coolpeace.core.common.BaseTimeEntity;
import com.coolpeace.core.domain.accommodation.entity.Accommodation;
import com.coolpeace.core.domain.coupon.entity.CouponRooms;
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

    private int roomNumber;

    private String roomType;

    private int price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accommodation_id")
    private Accommodation accommodation;

    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<RoomReservation> roomReservations = new ArrayList<>();

    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<CouponRooms> couponRooms = new ArrayList<>();

}
