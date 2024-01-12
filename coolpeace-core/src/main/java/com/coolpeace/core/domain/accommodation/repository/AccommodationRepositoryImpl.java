package com.coolpeace.core.domain.accommodation.repository;

import com.coolpeace.core.domain.accommodation.entity.Accommodation;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.util.List;

import static com.coolpeace.core.domain.accommodation.entity.QAccommodation.accommodation;

public class AccommodationRepositoryImpl implements AccommodationRepositoryCustom {

    private JPAQueryFactory jpaQueryFactory;

    public AccommodationRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public List<Accommodation> findAllBySigunguName(String name) {
        return jpaQueryFactory.selectFrom(accommodation)
            .where(accommodation.sigungu.name.eq(name)).fetch();
    }
}
