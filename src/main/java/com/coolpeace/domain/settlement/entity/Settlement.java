package com.coolpeace.domain.settlement.entity;

import com.coolpeace.domain.accommodation.entity.Accommodation;
import com.coolpeace.domain.coupon.entity.Coupon;
import com.coolpeace.global.common.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
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
    @Column(nullable = false)
    private LocalDate couponUseDate = LocalDate.of(2020,1,1);
    @Column(nullable = false)
    private int couponCount = 0;
    @Column(nullable = false)
    private int discountPrice = 0;
    @Column(nullable = false)
    private int cancelPrice = 0;
    @Column(nullable = false)
    private int supplyPrice = 0;

    @Column(nullable = false)
    private int sumPrice = 0;
    private LocalDate completeAt;

    @ManyToOne
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    @ManyToOne
    @JoinColumn(name = "accommodation_id")
    private Accommodation accommodation;

    public Settlement(Long id, LocalDate couponUseDate, int couponCount, int discountPrice,
        int cancelPrice, int supplyPrice, int sumPrice, LocalDate completeAt,Accommodation accommodation) {
        this.id = id;
        this.couponUseDate = couponUseDate;
        this.couponCount = couponCount;
        this.discountPrice = discountPrice;
        this.cancelPrice = cancelPrice;
        this.supplyPrice = supplyPrice;
        this.sumPrice = sumPrice;
        this.completeAt = completeAt;
        this.accommodation = accommodation;
    }

    public void completeSettlement() {
        this.completeAt = LocalDate.now();
    }

    public void sumPrice() {
        this.sumPrice = this.discountPrice + this.cancelPrice + this.supplyPrice;
    }
}
