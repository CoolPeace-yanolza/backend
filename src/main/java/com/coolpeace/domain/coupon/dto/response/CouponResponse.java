package com.coolpeace.domain.coupon.dto.response;

import com.coolpeace.domain.accommodation.entity.Accommodation;
import com.coolpeace.domain.coupon.entity.Coupon;
import com.coolpeace.global.common.DayOfWeekUtil;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public record CouponResponse(
        String title,
        String couponNumber,
        String couponStatus,
        String couponConcatTitle,
        String discountType,
        Integer discountValue,
        String customerType,
        String couponRoomType,
        Integer minimumReservationPrice,
        List<String> couponUseConditionDays,
        LocalDate exposureStartDate,
        LocalDate exposureEndDate,
        Integer couponExpiration,
        Integer downloadCount,
        Integer useCount,
        Long accommodationId,
        List<String> registerRoomNumbers,
        LocalDate createdDate
) {

    public static CouponResponse from(Coupon coupon) {
        return new CouponResponse(
                coupon.getTitle(),
                coupon.getCouponNumber(),
                coupon.getCouponStatus().getValue(),
                coupon.getCouponTitle(),
                coupon.getDiscountType().getValue(),
                coupon.getDiscountValue(),
                coupon.getCustomerType().getValue(),
                coupon.getCouponRoomType().getValue(),
                coupon.getMinimumReservationPrice(),
                DayOfWeekUtil.fromDayOfWeeks(coupon.getCouponUseConditionDays()),
                coupon.getExposureStartDate(),
                coupon.getExposureEndDate(),
                coupon.getCouponExpiration(),
                coupon.getDownloadCount(),
                coupon.getUseCount(),
                Optional.ofNullable(coupon.getAccommodation()).map(Accommodation::getId).orElse(null),
                coupon.getCouponRooms().stream().map(couponRooms -> couponRooms.getRoom().getRoomNumber()).toList(),
                coupon.getCreatedAt().toLocalDate()
        );
    }
}