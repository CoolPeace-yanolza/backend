package com.coolpeace.domain.coupon.entity;

import com.coolpeace.domain.accommodation.entity.Accommodation;
import com.coolpeace.domain.coupon.entity.converter.CouponUseConditionDaysTypeConverter;
import com.coolpeace.domain.coupon.entity.type.*;
import com.coolpeace.domain.member.entity.Member;
import com.coolpeace.domain.room.entity.Room;
import com.coolpeace.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import java.text.NumberFormat;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

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

    @OneToMany(mappedBy = "coupon", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CouponRooms> couponRooms = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

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
                  List<Room> rooms,
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
        updateCouponStatusByExposureDate();
        rooms.forEach(room -> this.couponRooms.add(CouponRooms.from(this, room)));
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
            List<Room> room,
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

    public String getCouponTitle() {
        NumberFormat numberFormat = NumberFormat.getInstance();
        if (this.discountType.equals(DiscountType.FIXED_PRICE)) {
            return this.customerType.getValue() + " " + numberFormat.format(this.discountValue)
                + "원 할인";
        }
        return this.customerType.getValue() + " " + numberFormat.format(this.discountValue)
            + "% 할인";
    }

    public void generateCouponNumber(CouponIssuerType couponIssuerType, Long id) {
        this.couponNumber =
            couponIssuerType.getValue() + String.format("%06d", Objects.requireNonNull(id));
    }

    public void changeCouponStatus(CouponStatusType couponStatusType) {
        this.couponStatus = couponStatusType;
    }

    public boolean betweenExposureDate(LocalDate localDate) {
        return !(localDate.isBefore(exposureStartDate) || localDate.isAfter(exposureEndDate));
    }

    private void updateCouponStatusByExposureDate() {
        if (this.betweenExposureDate(LocalDate.now())) {
            this.changeCouponStatus(CouponStatusType.EXPOSURE_ON);
        } else {
            this.changeCouponStatus(CouponStatusType.EXPOSURE_WAIT);
        }
    }

    public void updateCoupon(
            DiscountType discountType,
            Integer discountValue,
            CustomerType customerType,
            CouponRoomType couponRoomType,
            Integer minimumReservationPrice,
            List<DayOfWeek> couponUseConditionDays,
            List<Room> rooms,
            LocalDate exposureStartDate,
            LocalDate exposureEndDate
    ) {
        this.discountType = Optional.ofNullable(discountType).orElse(this.discountType);
        this.discountValue = Optional.ofNullable(discountValue).orElse(this.discountValue);
        this.customerType = Optional.ofNullable(customerType).orElse(this.customerType);
        this.couponRoomType = Optional.ofNullable(couponRoomType).orElse(this.couponRoomType);
        this.minimumReservationPrice = Optional.ofNullable(minimumReservationPrice).orElse(this.minimumReservationPrice);
        this.couponUseConditionDays = Optional.ofNullable(couponUseConditionDays).orElse(this.couponUseConditionDays);
        if (rooms != null) {
            this.couponRooms = rooms.stream().map(room -> CouponRooms.from(this, room)).toList();
        }
        this.exposureStartDate = Optional.ofNullable(exposureStartDate).orElse(this.exposureStartDate);
        this.exposureEndDate = Optional.ofNullable(exposureEndDate).orElse(this.exposureEndDate);
        updateCouponStatusByExposureDate();
    }

    public String getConcatTitle() {
        return switch (this.discountType) {
            case FIXED_PRICE -> String.format("%s %d원 할인", customerType.getValue(), discountValue);
            case FIXED_RATE -> String.format("%s %d%% 할인", customerType.getValue(), discountValue);
        };
    }
}
