package com.coolpeace.domain.coupon.entity;

import com.coolpeace.domain.accommodation.entity.Accommodation;
import com.coolpeace.domain.coupon.entity.converter.CouponUseConditionDaysTypeConverter;
import com.coolpeace.domain.coupon.entity.type.*;
import com.coolpeace.domain.member.entity.Member;
import com.coolpeace.domain.room.entity.Room;
import com.coolpeace.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
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

    private Integer maximumDiscountPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CustomerType customerType = CustomerType.ALL_CLIENT;

    @Enumerated(EnumType.STRING)
    private CouponRoomType couponRoomType;

    @Enumerated(EnumType.STRING)
    private CouponRoomType couponRoomStayType;

    private Integer minimumReservationPrice = 0;

    @Convert(converter = CouponUseConditionDaysTypeConverter.class)
    private final List<DayOfWeek> couponUseConditionDays = Collections.emptyList();

    @Enumerated(EnumType.STRING)
    private CouponUseDaysType couponUseDays = CouponUseDaysType.ALL;

    @Column(nullable = false)
    private LocalDate exposureStartDate;

    @Column(nullable = false)
    private LocalDate exposureEndDate;

    @Column(nullable = false)
    private Integer couponExpiration = 14;

    @Column(nullable = false)
    private Integer downloadCount = 0;

    @Column(nullable = false)
    private Integer useCount = 0;

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
                  Integer maximumDiscountPrice,
                  CustomerType customerType,
                  CouponRoomType couponRoomType,
                  CouponRoomType couponRoomStayType,
                  Integer minimumReservationPrice,
                  CouponUseDaysType couponUseDays,
                  LocalDate exposureStartDate,
                  LocalDate exposureEndDate,
                  Accommodation accommodation,
                  List<Room> rooms,
                  Member member) {
        this.title = title;
        this.discountType = discountType;
        this.discountValue = discountValue;
        this.maximumDiscountPrice = maximumDiscountPrice;
        this.customerType = customerType;
        this.couponRoomType = couponRoomType;
        this.couponRoomStayType = couponRoomStayType;
        this.minimumReservationPrice = minimumReservationPrice;
        this.couponUseDays = couponUseDays;
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
            Integer maximumDiscountPrice,
            CustomerType customerType,
            CouponRoomType couponRoomType,
            CouponRoomType couponRoomStayType,
            Integer minimumReservationPrice,
            CouponUseDaysType couponUseDays,
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
                maximumDiscountPrice,
                customerType,
                couponRoomType,
                couponRoomStayType,
                minimumReservationPrice,
                couponUseDays,
                exposureStartDate,
                exposureEndDate,
                accommodation,
                room,
                member
        );
    }

    public List<String> getCouponRoomTypeStringsExcludingTwoNight() {
        return Stream.of(this.getCouponRoomType(), this.getCouponRoomStayType())
                .filter(Objects::nonNull)
                .map(roomType -> (roomType == CouponRoomType.TWO_NIGHT) ? CouponRoomType.LODGE : roomType)
                .map(CouponRoomType::getValue).toList();
    }

    public String getCouponTitle() {
        return switch (this.discountType) {
            case FIXED_PRICE -> String.format("%s %d원 할인", customerType.getValue(), discountValue);
            case FIXED_RATE -> String.format("%s %d%% 할인", customerType.getValue(), discountValue);
        };
    }

    public void generateCouponNumber(CouponIssuerType couponIssuerType, Long id) {
        this.couponNumber =
            couponIssuerType.getValue() + String.format("%07d", Objects.requireNonNull(id));
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
            if (LocalDate.now().isAfter(exposureEndDate)) {
                this.changeCouponStatus(CouponStatusType.EXPOSURE_END);
            } else {
                this.changeCouponStatus(CouponStatusType.EXPOSURE_WAIT);
            }
        }
    }

    public void updateCoupon(
            DiscountType discountType,
            Integer discountValue,
            Integer maximumDiscountPrice,
            CustomerType customerType,
            CouponRoomType couponRoomType,
            CouponRoomType couponRoomStayType,
            Integer minimumReservationPrice,
            CouponUseDaysType couponUseDays,
            List<Room> rooms,
            LocalDate exposureStartDate,
            LocalDate exposureEndDate
    ) {
        this.title = Optional.ofNullable(title).orElse(this.title);
        this.discountType = Optional.ofNullable(discountType).orElse(this.discountType);
        this.discountValue = Optional.ofNullable(discountValue).orElse(this.discountValue);
        this.maximumDiscountPrice = Optional.ofNullable(maximumDiscountPrice).orElse(this.maximumDiscountPrice);
        this.customerType = Optional.ofNullable(customerType).orElse(this.customerType);
        this.couponRoomType = couponRoomType;
        this.couponRoomStayType = couponRoomStayType;
        this.minimumReservationPrice = Optional.ofNullable(minimumReservationPrice).orElse(this.minimumReservationPrice);
        this.couponUseDays = Optional.ofNullable(couponUseDays).orElse(this.couponUseDays);
        if (rooms != null) {
            this.couponRooms = rooms.stream().map(room -> CouponRooms.from(this, room)).toList();
        }
        this.exposureStartDate = Optional.ofNullable(exposureStartDate).orElse(this.exposureStartDate);
        this.exposureEndDate = Optional.ofNullable(exposureEndDate).orElse(this.exposureEndDate);
        updateCouponStatusByExposureDate();
    }

    public void addDownloadCount(){
        this.downloadCount += 1;
    }

    public void addUseCount(){
        this.useCount += 1;
    }

    public void setCouponExpiration(Integer couponExpiration){
        this.couponExpiration = couponExpiration;
    }
}
