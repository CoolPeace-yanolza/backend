package com.coolpeace.domain.settlement.dto.response;

public record SumSettlementResponse (
    int thisMonthSettlementAmount,
    int lastMonthSettlementAmount
){

    public static SumSettlementResponse from
        (int thisMonthSettlementAmount, int lastMonthSettlementAmount) {
        return new SumSettlementResponse(thisMonthSettlementAmount, lastMonthSettlementAmount);
    }
}
