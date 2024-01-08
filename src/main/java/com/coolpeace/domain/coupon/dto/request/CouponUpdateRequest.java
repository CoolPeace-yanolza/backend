package com.coolpeace.domain.coupon.dto.request;

import com.coolpeace.domain.coupon.entity.type.CouponRoomType;
import com.coolpeace.domain.coupon.entity.type.CustomerType;
import com.coolpeace.domain.coupon.entity.type.DiscountType;
import jakarta.validation.constraints.NotNull;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

public record CouponUpdateRequest(
        @NotNull(message = "숙박업체의 ID를 입력해야 합니다.")
        Long accommodationId,
        CustomerType customerType,
        DiscountType discountType,
        Integer discountValue,
        CouponRoomType couponRoomType,
        Boolean registerAllRoom,
        List<Integer> registerRooms,
        Integer minimumReservationPrice,
        List<DayOfWeek> couponUseConditionDays,
        LocalDate exposureStartDate,
        LocalDate exposureEndDate
) {
}
