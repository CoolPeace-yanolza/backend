package com.coolpeace.domain.statistics.repository;

import com.coolpeace.domain.accommodation.entity.Accommodation;
import com.coolpeace.domain.statistics.entity.MonthlyStatistics;
import java.util.List;

public interface MonthlyStatisticsRepositoryCustom {
    List<MonthlyStatistics> findLast6monthsMonthlyStatistics
        (Accommodation accommodation, int startYear, int startMonth,int endYear, int endMonth);
}
