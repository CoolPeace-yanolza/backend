package com.coolpeace.api.global.util;

import com.coolpeace.core.domain.room.entity.Room;
import com.github.javafaker.Faker;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RoomTestUtil {
    public static List<Room> getRandomRooms(List<Room> rooms) {
        Faker faker = new Faker(Locale.KOREA);
        Collections.shuffle(rooms);
        int roomCount = faker.number().numberBetween(1, rooms.size());
        return IntStream.range(0, roomCount)
                .mapToObj(rooms::get)
                .collect(Collectors.toList());
    }
}
