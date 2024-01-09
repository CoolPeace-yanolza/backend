package com.coolpeace.domain.dashboard.dto.response;

import com.coolpeace.domain.statistics.entity.MonthlyStatistics;

public record MonthlyDataLightResponse (
    int statisticsMonth,
    int totalSales,
    int couponTotalSales
){

    public static MonthlyDataLightResponse from(MonthlyStatistics monthlyStatistics) {
        return new MonthlyDataLightResponse(
            monthlyStatistics.getStatisticsMonth(),
            monthlyStatistics.getTotalSales(),
            monthlyStatistics.getCouponTotalSales()
        );
    }
}
