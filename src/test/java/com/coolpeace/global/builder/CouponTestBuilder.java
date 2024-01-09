package com.coolpeace.global.builder;

import com.coolpeace.domain.accommodation.entity.Accommodation;
import com.coolpeace.domain.coupon.entity.Coupon;
import com.coolpeace.domain.coupon.entity.type.CouponRoomType;
import com.coolpeace.domain.coupon.entity.type.CustomerType;
import com.coolpeace.domain.coupon.entity.type.DiscountType;
import com.coolpeace.domain.member.entity.Member;
import com.coolpeace.domain.room.entity.Room;
import com.github.javafaker.Faker;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CouponTestBuilder {
    private final Faker faker = new Faker();

    private final Accommodation accommodation;
    private final Member member;
    private final List<Room> rooms;

    public CouponTestBuilder(Accommodation accommodation, Member member, List<Room> rooms) {
        this.accommodation = accommodation;
        this.member = member;
        this.rooms = rooms;
    }

    public Coupon build() {
        String title = faker.lorem().characters(10, 20);

        DiscountType discountType = DiscountType.values()[faker.random().nextInt(DiscountType.values().length)];
        int discountValue = switch (discountType) {
            case FIXED_PRICE -> faker.random().nextInt(1, 300) * 100;
            case FIXED_RATE -> faker.random().nextInt(5, 50);
        };

        CustomerType customerType = CustomerType.values()[faker.random().nextInt(CustomerType.values().length)];
        CouponRoomType couponRoomType = CouponRoomType.values()[faker.random().nextInt(CouponRoomType.values().length)];

        boolean checkMinReservationPrice = faker.random().nextBoolean();
        int minimumReservationPrice = checkMinReservationPrice ? faker.random().nextInt(1, 100) * 100 : 0;

        boolean checkUseConditionDays = faker.random().nextBoolean();
        List<DayOfWeek> useConditionDays;
        if (checkUseConditionDays) {
            List<DayOfWeek> allDays = Arrays.asList(DayOfWeek.values());
            Collections.shuffle(allDays);
            useConditionDays = allDays.subList(0, faker.random().nextInt(allDays.size()));
        } else {
            useConditionDays = Collections.emptyList();
        }

        long startDate = faker.number().numberBetween(-200, 200);
        long period = faker.random().nextInt(100);
        LocalDate exposureStartDate = LocalDate.now().plusDays(startDate);
        LocalDate exposureEndDate = exposureStartDate.plusDays(period);

        return Coupon.from(
                title,
                discountType,
                discountValue,
                customerType,
                couponRoomType,
                minimumReservationPrice,
                useConditionDays,
                exposureStartDate,
                exposureEndDate,
                accommodation,
                rooms,
                member
        );
    }
}
