package com.coolpeace.domain.statistics.repository;

import static com.coolpeace.domain.statistics.entity.QMonthlyStatistics.monthlyStatistics;

import com.coolpeace.domain.accommodation.entity.Accommodation;
import com.coolpeace.domain.statistics.entity.MonthlyStatistics;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class MonthlyStatisticsRepositoryImpl implements MonthlyStatisticsRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public MonthlyStatisticsRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public List<MonthlyStatistics> findLast6monthsMonthlyStatistics(Accommodation accommodation,
        int startYear, int startMonth,int endYear, int endMonth) {

        if (startYear == endYear) {
            return jpaQueryFactory.selectFrom(monthlyStatistics)
                .where(monthlyStatistics.accommodation.eq(accommodation)
                    .and(monthlyStatistics.statisticsYear.eq(startYear))
                    .and(monthlyStatistics.statisticsMonth.goe(startMonth))
                    .and(monthlyStatistics.statisticsMonth.loe(endMonth)))
                .orderBy(monthlyStatistics.statisticsMonth.asc()).fetch();
        }

        List<MonthlyStatistics> fetch1 = jpaQueryFactory.selectFrom(monthlyStatistics)
            .where(monthlyStatistics.accommodation.eq(accommodation)
                .and(monthlyStatistics.statisticsYear.eq(startYear))
                .and(monthlyStatistics.statisticsMonth.goe(startMonth))
                .and(monthlyStatistics.statisticsMonth.loe(12)))
            .orderBy(monthlyStatistics.statisticsMonth.asc()).fetch();

        List<MonthlyStatistics> fetch2 = jpaQueryFactory.selectFrom(monthlyStatistics)
            .where(monthlyStatistics.accommodation.eq(accommodation)
                .and(monthlyStatistics.statisticsYear.eq(endYear))
                .and(monthlyStatistics.statisticsMonth.loe(endMonth))
                .and(monthlyStatistics.statisticsMonth.goe(1)))
            .orderBy(monthlyStatistics.statisticsMonth.asc()).fetch();

        return Stream.concat(fetch1.stream(), fetch2.stream()).sorted(Comparator.comparing(
                MonthlyStatistics::getStatisticsYear).thenComparing(MonthlyStatistics::getStatisticsMonth))
            .toList();
    }
}
