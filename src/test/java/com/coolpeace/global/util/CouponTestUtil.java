package com.coolpeace.global.util;

import com.coolpeace.domain.accommodation.entity.Accommodation;
import com.coolpeace.domain.coupon.entity.Coupon;
import com.coolpeace.domain.coupon.entity.type.CouponIssuerType;
import com.coolpeace.domain.coupon.entity.type.CouponStatusType;
import com.coolpeace.domain.member.entity.Member;
import com.coolpeace.domain.room.entity.Room;
import com.coolpeace.global.builder.CouponTestBuilder;
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
    public static List<Coupon> getExpiredTestCoupons(Accommodation accommodation, Member member, List<Room> rooms) {
        return IntStream.range(0, 20)
                .mapToObj(i -> {
                    List<Room> registerRooms = RoomTestUtil.getRandomRooms(rooms);
                    Coupon coupon = new CouponTestBuilder(accommodation, member, registerRooms).build();
                    ReflectionTestUtils.setField(coupon, "id", 4321L);
                    ReflectionTestUtils.setField(coupon, "createdAt", LocalDateTime.now().minusYears(1));
                    ReflectionTestUtils.setField(coupon, "exposureStartDate", coupon.getExposureStartDate().minusYears(1));
                    ReflectionTestUtils.setField(coupon, "exposureEndDate", coupon.getExposureEndDate().minusYears(1));
                    ReflectionTestUtils.setField(coupon, "createdAt", LocalDateTime.now().minusYears(1));
                    coupon.generateCouponNumber(CouponIssuerType.OWNER, coupon.getId());
                    coupon.changeCouponStatus(CouponStatusType.EXPOSURE_END);
                    return coupon;
                })
                .toList();
    }
}
