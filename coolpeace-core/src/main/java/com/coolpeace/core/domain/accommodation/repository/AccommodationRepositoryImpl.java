package com.coolpeace.domain.accommodation.repository;

import static com.coolpeace.domain.accommodation.entity.QAccommodation.accommodation;

import com.coolpeace.domain.accommodation.entity.Accommodation;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;

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
