package com.coolpeace.core.domain.settlement.entity;

import com.coolpeace.core.domain.accommodation.entity.Accommodation;
import com.coolpeace.core.domain.coupon.entity.Coupon;
import com.coolpeace.core.common.BaseTimeEntity;
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

    public void completeSettlement() {
        this.completeAt = LocalDate.now();
    }

    public void sumPrice() {
        this.sumPrice = this.discountPrice + this.cancelPrice + this.supplyPrice;
    }
}
