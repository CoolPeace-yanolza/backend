package com.coolpeace.domain.coupon.dto.response;

import com.coolpeace.domain.coupon.entity.Coupon;
import com.coolpeace.domain.coupon.entity.type.CouponRoomType;
import com.coolpeace.domain.coupon.entity.type.CouponStatusType;
import com.coolpeace.domain.coupon.entity.type.CustomerType;
import com.coolpeace.domain.coupon.entity.type.DiscountType;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

public record CouponResponse(
        String title,
        String couponNumber,
        CouponStatusType couponStatus,
        Boolean couponPromotion,
        String couponConcatTitle,
        DiscountType discountType,
        Integer discountValue,
        CustomerType customerType,
        CouponRoomType couponRoomType,
        Integer minimumReservationPrice,
        List<DayOfWeek> couponUseConditionDays,
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
                coupon.getCouponStatus(),
                false, // 프로모션이 개발되면 추후 수정 예정
                coupon.getConcatTitle(),
                coupon.getDiscountType(),
                coupon.getDiscountValue(),
                coupon.getCustomerType(),
                coupon.getCouponRoomType(),
                coupon.getMinimumReservationPrice(),
                coupon.getCouponUseConditionDays(),
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