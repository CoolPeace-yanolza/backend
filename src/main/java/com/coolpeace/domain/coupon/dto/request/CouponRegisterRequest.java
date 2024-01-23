package com.coolpeace.domain.coupon.dto.request;

import com.coolpeace.domain.coupon.entity.type.CouponUseDaysType;
import com.coolpeace.domain.coupon.entity.type.CustomerType;
import com.coolpeace.domain.coupon.entity.type.DiscountType;
import com.coolpeace.global.common.validator.ValidEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

public record CouponRegisterRequest(
        @NotBlank(message = "쿠폰의 이름을 입력해야 합니다.")
        @Size(max = 20, message = "쿠폰의 이름은 20자 내외로 입력해야 합니다.")
        String title,
        @NotNull(message = "고객의 유형을 선택해야 합니다.")
        @ValidEnum(enumClass = CustomerType.class, message = "올바르지 않은 고객의 유형입니다.")
        String customerType,

        // 할인 정책
        @NotNull(message = "할인의 유형을 선택해야 합니다.")
        @ValidEnum(enumClass = DiscountType.class, message = "올바르지 않은 할인의 유형입니다.")
        String discountType,
        Integer discountFlatValue,
        Integer discountFlatRate,
        Integer maximumDiscountPrice,

        // 객실 유형
        List<String> couponRoomTypes,

        // 숙소 ID
        @NotNull(message = "숙박업체의 ID를 입력해야 합니다.")
        Long accommodationId,

        // 방 등록
        @NotNull(message = "객실 등록 여부를 입력해야 합니다.")
        Boolean registerAllRoom,
        List<String> registerRooms,

        // 쿠폰 사용 조건
        Integer minimumReservationPrice,
        @ValidEnum(enumClass = CouponUseDaysType.class, message = "올바르지 않은 사용 조건의 날짜 유형입니다.", required = false)
        String couponUseConditionDays,

        // 노출 날짜
        @NotNull(message = "노출 시작 날짜를 입력해야 합니다.")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate exposureStartDate,
        @NotNull(message = "노출 종료 날짜를 입력해야 합니다.")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate exposureEndDate
) {
}
