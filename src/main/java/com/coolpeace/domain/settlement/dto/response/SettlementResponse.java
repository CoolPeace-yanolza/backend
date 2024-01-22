package com.coolpeace.domain.settlement.dto.response;

import com.coolpeace.domain.settlement.entity.Settlement;
import java.time.LocalDate;

public record SettlementResponse(
    LocalDate couponUseDate,
    String couponNumber,
    String couponName,
    int couponCount,
    int discountPrice,
    int cancelPrice,
    int sumPrice,
    LocalDate completeAt
) {

    public static SettlementResponse from(Settlement settlement) {
        return new SettlementResponse(
            settlement.getCouponUseDate(),
            settlement.getCoupon().getCouponNumber(),
            settlement.getCoupon().getCouponTitle(),
            settlement.getCouponCount(),
            settlement.getDiscountPrice(),
            settlement.getCancelPrice(),
            settlement.getSumPrice(),
            settlement.getCompleteAt()
        );
    }

    public static SettlementResponse from(LocalDate couponUseDate, String couponNumber,
        String couponName, int couponCount, int discountPrice, int cancelPrice,
        int sumPrice, LocalDate completeAt) {
        return new SettlementResponse(
            couponUseDate,
            couponNumber,
            couponName,
            couponCount,
            discountPrice,
            cancelPrice,
            sumPrice,
            completeAt
        );
    }

}
