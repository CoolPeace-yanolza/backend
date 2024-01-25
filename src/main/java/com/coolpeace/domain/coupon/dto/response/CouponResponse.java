package com.coolpeace.domain.coupon.dto.response;

import com.coolpeace.domain.accommodation.entity.Accommodation;
import com.coolpeace.domain.coupon.dto.request.type.DtoCouponUseDayOfWeekType;
import com.coolpeace.domain.coupon.dto.request.type.DtoCouponUseDaysType;
import com.coolpeace.domain.coupon.entity.Coupon;
import com.coolpeace.domain.coupon.entity.type.CouponUseDaysType;
import com.coolpeace.domain.coupon.entity.type.DiscountType;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public record CouponResponse(
        String title,
        String couponNumber,
        String couponStatus,
        String couponConcatTitle,
        String discountType,
        Integer discountFlatValue,
        Integer discountFlatRate,
        Integer maximumDiscountPrice,
        String customerType,
        List<String> couponRoomTypes,
        Integer minimumReservationPrice,
        String couponUseConditionDays,
        String couponUseConditionDayOfWeek,
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
                coupon.getDiscountType().equals(DiscountType.FIXED_PRICE) ? coupon.getDiscountValue() : null,
                coupon.getDiscountType().equals(DiscountType.FIXED_RATE) ? coupon.getDiscountValue() : null,
                coupon.getMaximumDiscountPrice(),
                coupon.getCustomerType().getValue(),
                coupon.getCouponRoomTypeStrings(),
                coupon.getMinimumReservationPrice(),
                filteringCouponUseConditionDays(coupon.getCouponUseDays()),
                filteringCouponUseConditionDayOfWeek(coupon.getCouponUseDays()),
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

    private static String filteringCouponUseConditionDays(CouponUseDaysType couponUseDays) {
        if (couponUseDays.isOneDay()) {
            return DtoCouponUseDaysType.ONEDAY.getValue();
        }
        return couponUseDays.getValue();
    }

    private static String filteringCouponUseConditionDayOfWeek(CouponUseDaysType couponUseDays) {
        if (couponUseDays.isOneDay()) {
            return DtoCouponUseDayOfWeekType.valueOf(couponUseDays.name()).getValue();
        }
        return null;
    }
}