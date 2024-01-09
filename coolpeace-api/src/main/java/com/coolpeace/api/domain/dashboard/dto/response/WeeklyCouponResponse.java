package com.coolpeace.api.domain.dashboard.dto.response;

import com.coolpeace.api.domain.statistics.entity.DailyStatistics;

public record WeeklyCouponResponse(
    int statisticsDay,
    int totalSales,
    int usedCount,
    int settlementAmount) {

    public static WeeklyCouponResponse from(DailyStatistics dailyStatistics) {
        return new WeeklyCouponResponse(
            dailyStatistics.getStatisticsDay(),
            dailyStatistics.getTotalSales(),
            dailyStatistics.getUsedCount(),
            dailyStatistics.getSettlementAmount()
        );
    }

}
