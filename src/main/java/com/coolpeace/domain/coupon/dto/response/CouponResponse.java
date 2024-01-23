package com.coolpeace.domain.coupon.dto.response;

import com.coolpeace.domain.accommodation.entity.Accommodation;
import com.coolpeace.domain.coupon.entity.Coupon;
import com.coolpeace.domain.coupon.entity.type.CouponRoomType;
import com.coolpeace.domain.coupon.entity.type.DiscountType;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

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
                Stream.of(coupon.getCouponRoomType(), coupon.getCouponRoomStayType())
                        .filter(Objects::nonNull).map(CouponRoomType::getValue).toList(),
                coupon.getMinimumReservationPrice(),
                coupon.getCouponUseDays().getValue(),
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