package com.coolpeace.api.domain.coupon.dto.response;

import com.coolpeace.core.domain.coupon.entity.Coupon;
import com.coolpeace.core.domain.coupon.entity.type.CouponRoomType;
import com.coolpeace.core.domain.coupon.entity.type.CouponStatusType;
import com.coolpeace.core.domain.coupon.entity.type.CustomerType;
import com.coolpeace.core.domain.coupon.entity.type.DiscountType;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

public record CouponResponse(
        String title,
        String couponNumber,
        String couponStatus,
        Boolean couponPromotion,
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
                false, // 프로모션이 개발되면 추후 수정 예정
                coupon.getCouponTitle(),
                coupon.getDiscountType().getValue(),
                coupon.getDiscountValue(),
                coupon.getCustomerType().getValue(),
                coupon.getCouponRoomType().getValue(),
                coupon.getMinimumReservationPrice(),
                coupon.getCouponUseConditionDays()
                        .stream().map(dayOfWeek -> dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.KOREAN))
                        .toList(),
                coupon.getExposureStartDate(),
                coupon.getExposureEndDate(),
                coupon.getCouponExpiration(),
                coupon.getDownloadCount(),
                coupon.getUseCount(),
                coupon.getAccommodation() != null ? coupon.getAccommodation().getId() : null,
                coupon.getCouponRooms().stream()
                        .map(couponRooms -> couponRooms.getRoom().getRoomNumber() + "호").toList(),
                coupon.getCreatedAt().toLocalDate()
        );
    }
}