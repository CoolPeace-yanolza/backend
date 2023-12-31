package com.coolpeace.global.builder;

import com.coolpeace.domain.accommodation.entity.Accommodation;
import com.coolpeace.domain.accommodation.entity.Sido;
import com.coolpeace.domain.accommodation.entity.Sigungu;
import com.coolpeace.domain.member.entity.Member;
import com.github.javafaker.Faker;

import java.util.Locale;

public class AccommodationTestBuilder {
    private final Faker faker = new Faker(Locale.KOREA);

    private final Member member;

    public AccommodationTestBuilder(Member member) {
        this.member = member;
    }

    public Accommodation build() {
        Sido sido = Sido.from(faker.address().state());
        return Accommodation.from(
                faker.name().firstName(),
                sido,
                Sigungu.from(faker.address().cityName(), sido),
                faker.address().streetName(),
                member);
    }
}
