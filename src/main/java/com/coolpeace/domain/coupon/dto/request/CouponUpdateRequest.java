package com.coolpeace.domain.coupon.dto.request;

import com.coolpeace.domain.coupon.entity.type.CouponRoomType;
import com.coolpeace.domain.coupon.entity.type.CustomerType;
import com.coolpeace.domain.coupon.entity.type.DiscountType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

public record CouponUpdateRequest(
        @Size(max = 20, message = "쿠폰의 이름은 20자 내외로 입력해야 합니다.")
        String title,
        CustomerType customerType,
        DiscountType discountType,
        Integer discountValue,
        CouponRoomType couponRoomType,
        Boolean registerAllRoom,
        List<Integer> registerRooms,
        Integer minimumReservationPrice,
        List<DayOfWeek> couponUseConditionDays,
        @NotNull(message = "숙박업체의 ID를 입력해야 합니다.")
        Long accommodationId,
        LocalDate exposureStartDate,
        LocalDate exposureEndDate
) {
}
