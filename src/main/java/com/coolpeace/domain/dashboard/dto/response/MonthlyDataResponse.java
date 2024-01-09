package com.coolpeace.domain.dashboard.dto.response;


import com.coolpeace.domain.statistics.entity.MonthlyStatistics;

public record MonthlyDataResponse(
    int statisticsYear,
    int statisticsMonth,
    int totalSales,
    int couponTotalSales,
    int downloadCount,
    int usedCount,
    int settlementAmount) {

    public static MonthlyDataResponse from(MonthlyStatistics monthlyStatistics) {
        return new MonthlyDataResponse(
            monthlyStatistics.getStatisticsYear(),
            monthlyStatistics.getStatisticsMonth(),
            monthlyStatistics.getTotalSales(),
            monthlyStatistics.getCouponTotalSales(),
            monthlyStatistics.getDownloadCount(),
            monthlyStatistics.getUsedCount(),
            monthlyStatistics.getSettlementAmount()
        );
    }
}
