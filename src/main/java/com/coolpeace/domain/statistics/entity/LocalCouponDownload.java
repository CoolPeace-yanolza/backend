package com.coolpeace.domain.statistics.entity;

import com.coolpeace.domain.accommodation.entity.type.AccommodationType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LocalCouponDownload {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(value = 2000)
    private int statisticsYear;

    @Min(value = 1)
    @Max(value = 12)
    private int statisticsMonth;

    @Column(nullable = false)
    private String region;

    @Column(nullable = false)
    private String firstCouponTitle="";

    private String secondCouponTitle;

    private String thirdCouponTitle;

    private Long firstCouponId;

    private Long secondCouponId;

    private Long thirdCouponId;

    @Column(nullable = false)
    private int motelCouponCount = 0;

    @Column(nullable = false)
    private int motelAccommodationCount = 0;

    @Column(nullable = false)
    private int pensionAndVillaCouponCount = 0;

    @Column(nullable = false)
    private int pensionAndVillaAccommodationCount = 0;

    @Column(nullable = false)
    private int hotelAndResortCouponCount = 0;

    @Column(nullable = false)
    private int hotelAndResortAccommodationCount = 0;

    public LocalCouponDownload(String region,int year, int month) {
        this.region = region;
        this.statisticsYear = year;
        this.statisticsMonth = month;
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

    public void setThirdCoupon(String thirdCouponTitle, Long thirdCouponId) {
        this.thirdCouponTitle = thirdCouponTitle;
        this.thirdCouponId = thirdCouponId;
    }

    public void setAllCoupon(String firstCouponTitle, Long firstCouponId,
        LocalCouponDownload localCouponDownload) {
        this.thirdCouponTitle = localCouponDownload.getSecondCouponTitle();
        this.thirdCouponId = localCouponDownload.getSecondCouponId();
        this.secondCouponTitle = localCouponDownload.getFirstCouponTitle();
        this.secondCouponId = localCouponDownload.getFirstCouponId();
        this.firstCouponTitle = firstCouponTitle;
        this.firstCouponId = firstCouponId;
    }

    public void setSecondAndThirdCoupon(String secondCouponTitle, Long secondCouponId,
        LocalCouponDownload localCouponDownload) {
        this.thirdCouponTitle = localCouponDownload.getSecondCouponTitle();
        this.thirdCouponId = localCouponDownload.getSecondCouponId();
        this.secondCouponTitle = secondCouponTitle;
        this.secondCouponId = secondCouponId;
    }

    public void setCount(int couponCount, AccommodationType type) {
        if (type.equals(AccommodationType.MOTEL)) {
            this.motelCouponCount += couponCount;
            this.motelAccommodationCount +=1;
        }
        if (type.equals(AccommodationType.PENSION) || (type.equals(AccommodationType.VILLA))) {
            this.pensionAndVillaCouponCount += couponCount;
            this.pensionAndVillaAccommodationCount += 1;
        }
        if (type.equals(AccommodationType.HOTEL) || (type.equals(AccommodationType.RESORT))) {
            this.hotelAndResortCouponCount += couponCount;
            this.hotelAndResortAccommodationCount += 1;
        }
    }

}
