package com.coolpeace.core.domain.statistics.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LocalCouponDownload {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String region;

    private String firstCouponTitle;

    private String secondCouponTitle;

    private String thirdCouponTitle;

    private Long firstCouponId;

    private Long secondCouponId;

    private Long thirdCouponId;

    public LocalCouponDownload(String region) {
        this.region = region;
    }

    public LocalCouponDownload(Long id, String region, String firstCouponTitle,
        String secondCouponTitle, String thirdCouponTitle) {
        this.id = id;
        this.region = region;
        this.firstCouponTitle = firstCouponTitle;
        this.secondCouponTitle = secondCouponTitle;
        this.thirdCouponTitle = thirdCouponTitle;
    }

    public void init(String couponTitle, Long couponId) {
        this.firstCouponTitle = couponTitle;
        this.secondCouponTitle = couponTitle;
        this.thirdCouponTitle = couponTitle;
        this.firstCouponId = couponId;
        this.secondCouponId = couponId;
        this.thirdCouponId = couponId;
    }

    public void setFirstCoupon(String firstCouponTitle, Long firstCouponId) {
        this.firstCouponTitle = firstCouponTitle;
        this.firstCouponId = firstCouponId;
    }

    public void setSecondCoupon(String secondCouponTitle, Long secondCouponId) {
        this.secondCouponTitle = secondCouponTitle;
        this.secondCouponId = secondCouponId;
    }

    public void setThirdCoupon(String thirdCouponTitle, Long thirdCouponId) {
        this.thirdCouponTitle = thirdCouponTitle;
        this.thirdCouponId = thirdCouponId;
    }

    public void setAllCoupon(String firstCouponTitle, Long firstCouponId,
        LocalCouponDownload localCouponDownload) {
        this.firstCouponTitle = firstCouponTitle;
        this.firstCouponId = firstCouponId;
        this.secondCouponTitle = localCouponDownload.getFirstCouponTitle();
        this.secondCouponId = localCouponDownload.getFirstCouponId();
        this.thirdCouponTitle = localCouponDownload.getSecondCouponTitle();
        this.thirdCouponId = localCouponDownload.getSecondCouponId();
    }
    public void setSecondAndThirdCoupon(String secondCouponTitle, Long secondCouponId,
        LocalCouponDownload localCouponDownload) {
        this.secondCouponTitle = secondCouponTitle;
        this.secondCouponId = secondCouponId;
        this.thirdCouponTitle = localCouponDownload.getSecondCouponTitle();
        this.thirdCouponId = localCouponDownload.getSecondCouponId();
    }

}
