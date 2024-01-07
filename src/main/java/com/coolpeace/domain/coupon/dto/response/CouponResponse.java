package com.coolpeace.domain.coupon.dto.response;

import com.coolpeace.domain.coupon.entity.Coupon;
import com.coolpeace.domain.coupon.entity.type.CouponRoomType;
import com.coolpeace.domain.coupon.entity.type.CouponStatusType;
import com.coolpeace.domain.coupon.entity.type.CustomerType;
import com.coolpeace.domain.coupon.entity.type.DiscountType;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record CouponResponse(
        String title,
        String couponNumber,
        CouponStatusType couponStatus,
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
        List<Integer> registerRoomNumbers,
        LocalDateTime createdDate
) {

    public static CouponResponse from(Coupon coupon, List<Integer> registerRoomNumbers) {
        return new CouponResponse(
                coupon.getTitle(),
                coupon.getCouponNumber(),
                coupon.getCouponStatus(),
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
                registerRoomNumbers,
                coupon.getCreatedAt()
        );
    }
}