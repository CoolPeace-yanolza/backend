package com.coolpeace.domain.settlement.entity;

import com.coolpeace.domain.accommodation.entity.Accommodation;
import com.coolpeace.domain.coupon.entity.Coupon;
import com.coolpeace.domain.member.entity.Member;
import com.coolpeace.global.common.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Settlement extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime startDate;
    private String couponTitle;
    private int couponCount;
    private int discountPrice;
    private int charge;
    private int cancelPrice;
    private LocalDateTime scheduleAt;
    private LocalDateTime completeAt;

    @ManyToOne
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    @ManyToOne
    @JoinColumn(name = "accommodation_id")
    private Accommodation accommodation;

}
