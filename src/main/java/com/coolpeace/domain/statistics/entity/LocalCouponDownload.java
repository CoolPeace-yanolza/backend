package com.coolpeace.domain.statistics.entity;

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
@Setter
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
}
