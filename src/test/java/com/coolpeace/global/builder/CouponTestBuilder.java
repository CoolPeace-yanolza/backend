package com.coolpeace.global.builder;

import com.coolpeace.domain.accommodation.entity.Accommodation;
import com.coolpeace.domain.coupon.entity.Coupon;
import com.coolpeace.domain.coupon.entity.type.CouponRoomType;
import com.coolpeace.domain.coupon.entity.type.CouponUseDaysType;
import com.coolpeace.domain.coupon.entity.type.CustomerType;
import com.coolpeace.domain.coupon.entity.type.DiscountType;
import com.coolpeace.domain.member.entity.Member;
import com.coolpeace.domain.room.entity.Room;
import com.github.javafaker.Faker;

import java.time.LocalDate;
import java.util.List;

public class CouponTestBuilder {
    private final Faker faker = new Faker();

    private final Accommodation accommodation;
    private final Member member;
    private final List<Room> registerRooms;

    private LocalDate exposureStartDate;
    private LocalDate exposureEndDate;

    public CouponTestBuilder(Accommodation accommodation, Member member, List<Room> registerRooms) {
        this.accommodation = accommodation;
        this.member = member;
        this.registerRooms = registerRooms;
    }

    public CouponTestBuilder exposureDates(LocalDate exposureStartDate, LocalDate exposureEndDate) {
        this.exposureStartDate = exposureStartDate;
        this.exposureEndDate = exposureEndDate;
        return this;
    }

    public CouponTestBuilder expiredExposureDates() {
        long startDate = faker.number().numberBetween(200, 300);
        long period = faker.random().nextInt(100);
        this.exposureStartDate = LocalDate.now().minusDays(startDate);
        this.exposureEndDate = exposureStartDate.plusDays(period);
        return this;
    }

    public CouponTestBuilder currentExposureDates() {
        long startDate = faker.number().numberBetween(0, 50);
        long period = faker.random().nextInt(70, 100);
        this.exposureStartDate = LocalDate.now().minusDays(startDate);
        this.exposureEndDate = exposureStartDate.plusDays(period);
        return this;
    }

    public Coupon build() {
        String title = faker.lorem().characters(10, 20);

        DiscountType discountType = DiscountType.values()[faker.random().nextInt(DiscountType.values().length)];
        int discountValue = switch (discountType) {
            case FIXED_PRICE -> faker.random().nextInt(1, 300) * 100;
            case FIXED_RATE -> faker.random().nextInt(5, 50);
        };
        Integer maximumDiscountPrice = null;
        if (discountType.equals(DiscountType.FIXED_RATE)) {
            maximumDiscountPrice = faker.random().nextBoolean() ? faker.random().nextInt(1, 10) * 1000 : null;
        }

        CustomerType customerType = CustomerType.values()[faker.random().nextInt(CustomerType.values().length)];
        CouponRoomType couponRoomType = CouponRoomType.values()[faker.random().nextInt(CouponRoomType.values().length)];

        boolean checkMinReservationPrice = faker.random().nextBoolean();
        int minimumReservationPrice = checkMinReservationPrice ? faker.random().nextInt(1, 100) * 100 : 0;

        CouponUseDaysType couponUseDays = CouponUseDaysType.values()[faker.random().nextInt(CouponUseDaysType.values().length)];

        if (exposureStartDate == null || exposureEndDate == null) {
            long startDate = faker.number().numberBetween(-200, 200);
            long period = faker.random().nextInt(100);
            this.exposureStartDate = LocalDate.now().plusDays(startDate);
            this.exposureEndDate = exposureStartDate.plusDays(period);
        }


        return Coupon.from(
                title,
                discountType,
                discountValue,
                maximumDiscountPrice,
                customerType,
                couponRoomType,
                minimumReservationPrice,
                couponUseDays,
                exposureStartDate,
                exposureEndDate,
                accommodation,
                registerRooms,
                member
        );
    }
}
