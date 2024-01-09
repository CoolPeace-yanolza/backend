package com.coolpeace.domain.settlement.dto.response;

import com.coolpeace.domain.settlement.entity.Settlement;
import java.time.LocalDate;

public record SettlementResponse (
    LocalDate couponUseDate,
    int couponCount,
    int discountPrice,
    int cancelPrice,
    int supplyPrice,
    int sumPrice,
    LocalDate completeAt
){

    public static SettlementResponse from(Settlement settlement) {
        return new SettlementResponse(
            settlement.getCouponUseDate(),
            settlement.getCouponCount(),
            settlement.getDiscountPrice(),
            settlement.getCancelPrice(),
            settlement.getSupplyPrice(),
            settlement.getSumPrice(),
            settlement.getCompleteAt()
        );
    }
    public static SettlementResponse from(LocalDate couponUseDate, int couponCount, int discountPrice,
        int cancelPrice, int supplyPrice, int sumPrice, LocalDate completeAt) {
        return new SettlementResponse(
            couponUseDate,
            couponCount,
            discountPrice,
            cancelPrice,
            supplyPrice,
            sumPrice,
            completeAt
        );
    }

}
