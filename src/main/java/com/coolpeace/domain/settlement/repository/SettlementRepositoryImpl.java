package com.coolpeace.domain.settlement.repository;

import static com.coolpeace.domain.settlement.entity.QSettlement.settlement;

import com.coolpeace.domain.accommodation.entity.Accommodation;
import com.coolpeace.domain.settlement.entity.Settlement;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

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
