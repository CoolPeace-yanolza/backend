package com.coolpeace.domain.coupon.entity;

import com.coolpeace.domain.coupon.entity.key.CouponRoomsId;
import com.coolpeace.domain.room.entity.Room;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CouponRooms {
    @EmbeddedId
    private CouponRoomsId couponRoomsId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @MapsId("couponId")
    @JoinColumn(nullable = false)
    private Coupon coupon;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @MapsId("roomId")
    private Room room;

    private CouponRooms(Coupon coupon, Room room) {
        this.couponRoomsId = CouponRoomsId.from(coupon.getId(), room.getId());
        this.coupon = coupon;
        this.room = room;
    }

    public static CouponRooms from(Coupon coupon, Room room) {
        return new CouponRooms(coupon, room);
    }
}
