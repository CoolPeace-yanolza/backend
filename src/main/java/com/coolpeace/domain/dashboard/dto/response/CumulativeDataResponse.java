package com.coolpeace.domain.dashboard.dto.response;

public record CumulativeDataResponse(
    int couponTotalSales,
    int couponUseSales,
    int couponTotalUsedCount,
    int couponTotalDownloadCount) {

    public static CumulativeDataResponse from (int couponTotalSales, int couponUseSales,
        int couponTotalUsedCount, int couponTotalDownloadCount ) {
        return new CumulativeDataResponse(
            couponTotalSales, couponUseSales, couponTotalUsedCount, couponTotalDownloadCount);
    }
}
