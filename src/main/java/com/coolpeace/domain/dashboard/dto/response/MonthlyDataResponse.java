package com.coolpeace.domain.dashboard.dto.response;


import com.coolpeace.domain.statistics.entity.MonthlyStatistics;

public record MonthlyDataResponse(
    int statisticsYear,
    int statisticsMonth,
    int totalSales,
    int couponTotalSales,
    int downloadCount,
    int usedCount,
    int settlementAmount,
    int conversionRate) {

    public static MonthlyDataResponse from(MonthlyStatistics monthlyStatistics) {
        String conversionRate ="0";
        if(monthlyStatistics.getDownloadCount() !=0){
            conversionRate = String.format("%.0f",
                ((double) monthlyStatistics.getUsedCount() /
                    monthlyStatistics.getDownloadCount()) * 100);
        }
        return new MonthlyDataResponse(
            monthlyStatistics.getStatisticsYear(),
            monthlyStatistics.getStatisticsMonth(),
            monthlyStatistics.getTotalSales(),
            monthlyStatistics.getCouponTotalSales(),
            monthlyStatistics.getDownloadCount(),
            monthlyStatistics.getUsedCount(),
            monthlyStatistics.getSettlementAmount(),
            Integer.parseInt(conversionRate)
        );
    }
}
