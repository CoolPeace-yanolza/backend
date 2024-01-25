package com.coolpeace.domain.settlement.dto.response;

import java.util.List;

public record PageSettlementResponse(
    Long totalSettlementCount,
    int totalPageCount,
    int totalCouponCount,
    int totalDiscountPrice,
    int totalCancelPrice,
    int totalSumPrice,
    List<SettlementResponse> settlementResponses
) {

    public static PageSettlementResponse from(Long totalSettlementCount, int totalPageCount,
        int totalCouponCount, int totalDiscountPrice, int totalCancelPrice, int totalSumPrice,
        List<SettlementResponse> settlementResponses) {
        return new PageSettlementResponse(
            totalSettlementCount,
            totalPageCount,
            totalCouponCount,
            totalDiscountPrice,
            totalCancelPrice,
            totalSumPrice,
            settlementResponses);
    }

}
