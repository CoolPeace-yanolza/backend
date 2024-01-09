package com.coolpeace.api.domain.settlement.dto.response;

public record SumSettlementResponse (
    int thisMonthSettlementAmount,
    int lastMonthSettlementAmount
){

    public static SumSettlementResponse from
        (int thisMonthSettlementAmount, int lastMonthSettlementAmount) {
        return new SumSettlementResponse(thisMonthSettlementAmount, lastMonthSettlementAmount);
    }
}
