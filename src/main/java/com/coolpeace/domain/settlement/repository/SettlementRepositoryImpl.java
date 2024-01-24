package com.coolpeace.domain.settlement.repository;

import static com.coolpeace.domain.settlement.entity.QSettlement.settlement;

import com.coolpeace.domain.accommodation.entity.Accommodation;
import com.coolpeace.domain.data.dto.request.SettlementStatistic;
import com.coolpeace.domain.settlement.entity.Settlement;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SettlementRepositoryImpl implements SettlementRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Settlement> findAllByAccommodationForDailyUpdate(LocalDate localDate,Accommodation accommodation) {

        return jpaQueryFactory.selectFrom(settlement)
            .where(settlement.accommodation.eq(accommodation)
                .and(settlement.couponUseDate.eq(localDate))).fetch();
    }

}
