package com.coolpeace.api.global.util;

import com.coolpeace.api.global.builder.CouponTestBuilder;
import com.coolpeace.core.domain.accommodation.entity.Accommodation;
import com.coolpeace.core.domain.coupon.entity.Coupon;
import com.coolpeace.core.domain.coupon.entity.type.CouponIssuerType;
import com.coolpeace.core.domain.member.entity.Member;
import com.coolpeace.core.domain.room.entity.Room;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;

public class CouponTestUtil {
    public static List<Coupon> getTestCoupons(Accommodation accommodation, Member member, List<Room> rooms) {
        return IntStream.range(0, 20)
                .mapToObj(i -> {
                    List<Room> registerRooms = RoomTestUtil.getRandomRooms(rooms);
                    Coupon coupon = new CouponTestBuilder(accommodation, member, registerRooms).build();
                    ReflectionTestUtils.setField(coupon, "id", 4321L);
                    ReflectionTestUtils.setField(coupon, "createdAt", LocalDateTime.now());
                    coupon.generateCouponNumber(CouponIssuerType.OWNER, coupon.getId());
                    return coupon;
                })
                .toList();
    }
}
