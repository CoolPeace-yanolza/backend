package com.coolpeace.api.domain.settlement.repository;

import com.coolpeace.api.domain.accommodation.entity.Accommodation;
import com.coolpeace.api.domain.settlement.entity.Settlement;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.coolpeace.api.domain.settlement.entity.QSettlement.settlement;

@RequiredArgsConstructor
public class SettlementRepositoryImpl implements SettlementRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Settlement> findAllByAccommodationForDailyUpdate(Accommodation accommodation) {

        return jpaQueryFactory.selectFrom(settlement)
            .where(settlement.accommodation.eq(accommodation)
                .and(settlement.completeAt.isNull())).fetch();
    }

}
