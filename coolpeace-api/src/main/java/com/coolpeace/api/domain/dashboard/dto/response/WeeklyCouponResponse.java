package com.coolpeace.api.domain.dashboard.dto.response;

import com.coolpeace.core.domain.statistics.entity.DailyStatistics;
import com.coolpeace.core.domain.statistics.entity.DailyStatistics;
import java.util.List;

public record WeeklyCouponResponse(
    int couponTotalSales,
    int usedCount,
    int settlementAmount,
    int downloadCount) {

    public static WeeklyCouponResponse from(List<DailyStatistics> dailyStatisticsList) {
        int couponTotalSales = 0;
        int usedCount = 0;
        int settlementAmount = 0;
        int downloadCount = 0;
        for (DailyStatistics dailyStatistics : dailyStatisticsList) {
            couponTotalSales += dailyStatistics.getCouponTotalSales();
            usedCount += dailyStatistics.getUsedCount();
            settlementAmount += dailyStatistics.getSettlementAmount();
            downloadCount += dailyStatistics.getDownloadCount();
        }
        return new WeeklyCouponResponse(
            couponTotalSales,
            usedCount,
            settlementAmount,
            downloadCount
        );
    }
}
