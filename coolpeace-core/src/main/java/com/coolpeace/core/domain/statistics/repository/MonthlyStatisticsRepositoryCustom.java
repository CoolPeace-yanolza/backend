package com.coolpeace.core.domain.statistics.repository;

import com.coolpeace.core.domain.accommodation.entity.Accommodation;
import com.coolpeace.core.domain.statistics.entity.MonthlyStatistics;
import java.util.List;

public interface MonthlyStatisticsRepositoryCustom {
    List<MonthlyStatistics> findLast6monthsMonthlyStatistics
        (Accommodation accommodation, int startYear, int startMonth,int endYear, int endMonth);
}
