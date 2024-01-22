package com.coolpeace.domain.settlement.dto.response;

import java.util.List;

public record PageSettlementResponse(
    Long totalSettlementCount,
    int totalPageCount,
    List<SettlementResponse> settlementResponses
) {

    public static PageSettlementResponse from(Long totalSettlementCount, int totalPageCount,
        List<SettlementResponse> settlementResponses) {
        return new PageSettlementResponse(
            totalSettlementCount,
            totalPageCount,
            settlementResponses);
    }

}
