package com.coolpeace.domain.coupon.entity;

import com.coolpeace.domain.accommodation.entity.Accommodation;
import com.coolpeace.domain.coupon.entity.converter.CouponRoomTypeConverter;
import com.coolpeace.domain.coupon.entity.converter.CouponUseConditionDaysTypeConverter;
import com.coolpeace.domain.coupon.entity.type.CouponRoomType;
import com.coolpeace.domain.coupon.entity.type.CustomerType;
import com.coolpeace.domain.coupon.entity.type.DiscountType;
import com.coolpeace.domain.member.entity.Member;
import com.coolpeace.domain.room.entity.Room;
import com.coolpeace.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coupon extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private CouponStatusType couponStatus = CouponStatusType.EXPOSURE_WAIT;

    @Column(nullable = false)
    private DiscountType discountType = DiscountType.FIXED_PRICE;

    private Integer discountValue;

    @Column(nullable = false)
    private CustomerType customerType = CustomerType.ALL_CLIENT;

    @Convert(converter = CouponRoomTypeConverter.class)
    @Column(nullable = false)
    private List<CouponRoomType> couponRoomType;

    private Integer minimumReservationPrice = 0;

    @Convert(converter = CouponUseConditionDaysTypeConverter.class)
    private List<DayOfWeek> couponUseConditionDays;

    @Column(nullable = false)
    private LocalDateTime exposeStartDate;

    @Column(nullable = false)
    private LocalDateTime exposeEndDate;

    @Column(nullable = false)
    private LocalDateTime expirationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accommodation_id")
    private Accommodation accommodation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
}
