package com.coolpeace.domain.coupon.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record CouponUpdateRequest(
        @NotNull(message = "숙박업체의 ID를 입력해야 합니다.")
        Long accommodationId,
//        @ValidEnum(enumClass = CustomerType.class, message = "올바르지 않은 고객의 유형입니다.")
        String customerType,
//        @ValidEnum(enumClass = DiscountType.class, message = "올바르지 않은 고객의 유형입니다.")
        String discountType,
        Integer discountValue,
//        @ValidEnum(enumClass = CouponRoomType.class, message = "올바르지 않은 고객의 유형입니다.")
        String couponRoomType,
        Boolean registerAllRoom,
        List<String> registerRooms,
        Integer minimumReservationPrice,
        List<String> couponUseConditionDays,
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate exposureStartDate,
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate exposureEndDate
) {
}
