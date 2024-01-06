package com.coolpeace.domain.coupon.entity;

import com.coolpeace.domain.accommodation.entity.Accommodation;
import com.coolpeace.domain.coupon.entity.converter.CouponUseConditionDaysTypeConverter;
import com.coolpeace.domain.coupon.entity.type.*;
import com.coolpeace.domain.member.entity.Member;
import com.coolpeace.domain.room.entity.Room;
import com.coolpeace.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coupon extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String couponNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CouponStatusType couponStatus = CouponStatusType.EXPOSURE_WAIT;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DiscountType discountType;

    private Integer discountValue;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CustomerType customerType = CustomerType.ALL_CLIENT;

    @Column(nullable = false)
    private CouponRoomType couponRoomType;

    private Integer minimumReservationPrice = 0;

    @Convert(converter = CouponUseConditionDaysTypeConverter.class)
    private List<DayOfWeek> couponUseConditionDays;

    @Column(nullable = false)
    private LocalDate exposureStartDate;

    @Column(nullable = false)
    private LocalDate exposureEndDate;

    @Column(nullable = false)
    private final Integer couponExpiration = 14;

    @Column(nullable = false)
    private final Integer downloadCount = 0;

    @Column(nullable = false)
    private final Integer useCount = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accommodation_id")
    private Accommodation accommodation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    // 모든 방에 적용할 경우
    public Coupon(String title,
                  DiscountType discountType,
                  Integer discountValue,
                  CustomerType customerType,
                  CouponRoomType couponRoomType,
                  Integer minimumReservationPrice,
                  List<DayOfWeek> couponUseConditionDays,
                  LocalDate exposureStartDate,
                  LocalDate exposureEndDate,
                  Accommodation accommodation,
                  Member member) {
        this.title = title;
        this.discountType = discountType;
        this.discountValue = discountValue;
        this.customerType = customerType;
        this.couponRoomType = couponRoomType;
        this.minimumReservationPrice = minimumReservationPrice;
        this.couponUseConditionDays = couponUseConditionDays;
        this.exposureStartDate = exposureStartDate;
        this.exposureEndDate = exposureEndDate;
        this.accommodation = accommodation;
        this.member = member;
    }

    // 특정 방을 선택했을 경우
    public Coupon(String title,
                  DiscountType discountType,
                  Integer discountValue,
                  CustomerType customerType,
                  CouponRoomType couponRoomType,
                  Integer minimumReservationPrice,
                  List<DayOfWeek> couponUseConditionDays,
                  LocalDate exposureStartDate,
                  LocalDate exposureEndDate,
                  Accommodation accommodation,
                  Room room,
                  Member member) {
        this.title = title;
        this.discountType = discountType;
        this.discountValue = discountValue;
        this.customerType = customerType;
        this.couponRoomType = couponRoomType;
        this.minimumReservationPrice = minimumReservationPrice;
        this.couponUseConditionDays = couponUseConditionDays;
        this.exposureStartDate = exposureStartDate;
        this.exposureEndDate = exposureEndDate;
        this.accommodation = accommodation;
        this.room = room;
        this.member = member;
    }

    public static Coupon from(
            String title,
            DiscountType discountType,
            Integer discountValue,
            CustomerType customerType,
            CouponRoomType couponRoomType,
            Integer minimumReservationPrice,
            List<DayOfWeek> couponUseConditionDays,
            LocalDate exposureStartDate,
            LocalDate exposureEndDate,
            Accommodation accommodation,
            Room room,
            Member member
    ) {
        return new Coupon(
                title,
                discountType,
                discountValue,
                customerType,
                couponRoomType,
                minimumReservationPrice,
                couponUseConditionDays,
                exposureStartDate,
                exposureEndDate,
                accommodation,
                room,
                member
        );
    }

    public static Coupon from(
            String title,
            DiscountType discountType,
            Integer discountValue,
            CustomerType customerType,
            CouponRoomType couponRoomType,
            Integer minimumReservationPrice,
            List<DayOfWeek> couponUseConditionDays,
            LocalDate exposureStartDate,
            LocalDate exposureEndDate,
            Accommodation accommodation,
            Member member
    ) {
        return new Coupon(
                title,
                discountType,
                discountValue,
                customerType,
                couponRoomType,
                minimumReservationPrice,
                couponUseConditionDays,
                exposureStartDate,
                exposureEndDate,
                accommodation,
                member
        );
    }

    public void generateCouponNumber(CouponIssuerType couponIssuerType, Long id) {
        this.couponNumber = couponIssuerType.getValue() + String.format("%06d", Objects.requireNonNull(id));
    }

    public void changeCouponStatus(CouponStatusType couponStatusType) {
        this.couponStatus = couponStatusType;
    }

    public void updateCoupon(
            String title,
            DiscountType discountType,
            Integer discountValue,
            CustomerType customerType,
            CouponRoomType couponRoomType,
            Integer minimumReservationPrice,
            List<DayOfWeek> couponUseConditionDays,
            LocalDate exposureStartDate,
            LocalDate exposureEndDate
    ) {
        this.title = Optional.ofNullable(title).orElse(this.title);
        this.discountType = Optional.ofNullable(discountType).orElse(this.discountType);
        this.discountValue = Optional.ofNullable(discountValue).orElse(this.discountValue);
        this.customerType = Optional.ofNullable(customerType).orElse(this.customerType);
        this.couponRoomType = Optional.ofNullable(couponRoomType).orElse(this.couponRoomType);
        this.minimumReservationPrice = Optional.ofNullable(minimumReservationPrice).orElse(this.minimumReservationPrice);
        this.couponUseConditionDays = Optional.ofNullable(couponUseConditionDays).orElse(this.couponUseConditionDays);
        this.exposureStartDate = Optional.ofNullable(exposureStartDate).orElse(this.exposureStartDate);
        this.exposureEndDate = Optional.ofNullable(exposureEndDate).orElse(this.exposureEndDate);
    }

    @PostPersist
    private void postPersist() {
        this.generateCouponNumber(CouponIssuerType.OWNER, this.id);
    }

}
