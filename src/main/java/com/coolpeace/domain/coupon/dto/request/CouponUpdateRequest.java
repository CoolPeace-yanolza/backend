package com.coolpeace.domain.coupon.dto.request;

import com.coolpeace.domain.coupon.dto.request.type.DtoCouponUseDayOfWeekType;
import com.coolpeace.domain.coupon.dto.request.type.DtoCouponUseDaysType;
import com.coolpeace.domain.coupon.entity.type.CustomerType;
import com.coolpeace.domain.coupon.entity.type.DiscountType;
import com.coolpeace.global.common.validator.ValidEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record CouponUpdateRequest(
        String title,
        @NotNull(message = "숙박업체의 ID를 입력해야 합니다.")
        Long accommodationId,
        @ValidEnum(enumClass = CustomerType.class, message = "올바르지 않은 고객의 유형입니다.", required = false)
        String customerType,
        @ValidEnum(enumClass = DiscountType.class, message = "올바르지 않은 할인의 유형입니다.", required = false)
        String discountType,
        Integer discountFlatValue,
        Integer discountFlatRate,
        Integer maximumDiscountPrice,
        List<String> couponRoomTypes,
        Boolean registerAllRoom,
        List<String> registerRooms,
        Integer minimumReservationPrice,
        @ValidEnum(enumClass = DtoCouponUseDaysType.class, message = "올바르지 않은 사용 조건의 날짜 유형입니다.", required = false)
        String couponUseConditionDays,
        @ValidEnum(enumClass = DtoCouponUseDayOfWeekType.class, message = "올바르지 않은 사용 조건의 요일입니다.", required = false)
        String couponUseConditionDayOfWeek,
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate exposureStartDate,
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate exposureEndDate
) {
}
