package com.coolpeace.global.builder;

import com.coolpeace.domain.accommodation.entity.Accommodation;
import com.coolpeace.domain.coupon.entity.type.CouponRoomType;
import com.coolpeace.domain.room.entity.Room;
import com.github.javafaker.Faker;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RoomTestBuilder {
    private final Faker faker = new Faker(Locale.KOREA);

    private final Accommodation accommodation;

    public RoomTestBuilder(Accommodation accommodation) {
        this.accommodation = accommodation;
    }

    public Room build() {
        return Room.from(
                faker.number().numberBetween(101, 1000),
                CouponRoomType.values()[faker.random().nextInt(CouponRoomType.values().length)].getValue(),
                faker.number().numberBetween(1, 100) * 10000,
                accommodation
        );
    }

    public List<Room> buildList() {
        int roomCount = faker.number().numberBetween(1, 20);
        List<Integer> roomNumberList = IntStream.range(101, 1000)
                .boxed().collect(Collectors.toList());
        Collections.shuffle(roomNumberList);
        return IntStream.range(0, roomCount)
                .mapToObj(i -> Room.from(
                        roomNumberList.get(i),
                        CouponRoomType.values()[faker.random().nextInt(CouponRoomType.values().length)].getValue(),
                        faker.number().numberBetween(1, 100) * 10000,
                        accommodation
                ))
                .collect(Collectors.toList());
    }
}
