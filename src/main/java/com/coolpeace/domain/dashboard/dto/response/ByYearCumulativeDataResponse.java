package com.coolpeace.domain.dashboard.dto.response;

import java.util.List;

public record ByYearCumulativeDataResponse(
    int couponTotalSales,
    int couponUseSales,
    int couponTotalUsedCount,
    List<MonthlyDataLightResponse> couponSalesList
) {

    public static ByYearCumulativeDataResponse from(int couponTotalSales, int couponUseSales,
        int couponTotalUsedCount, List<MonthlyDataLightResponse> couponSalesList) {
        return new ByYearCumulativeDataResponse
            (couponTotalSales, couponUseSales, couponTotalUsedCount, couponSalesList);
    }
}
