package com.coolpeace.api.domain.statistics.entity;

import com.coolpeace.api.domain.accommodation.entity.Accommodation;
import com.coolpeace.api.domain.member.entity.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MonthlyStatistics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(value = 2000)
    private int statisticsYear;

    @Min(value = 1)
    @Max(value = 12)
    private int statisticsMonth;

    @Column(nullable = false)
    private int totalSales = 0;

    @Column(nullable = false)
    private int couponTotalSales = 0;

    @Column(nullable = false)
    private int downloadCount = 0;

    @Column(nullable = false)
    private int usedCount = 0;

    @Column(nullable = false)
    private int settlementAmount = 0;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "accommodation_id")
    private Accommodation accommodation;

    @ManyToOne
    @JoinColumn(name = "localCouponDownload_id")
    private LocalCouponDownload localCouponDownload;

    public MonthlyStatistics(int statisticsYear, int statisticsMonth, Member member, Accommodation accommodation) {
        this.statisticsYear = statisticsYear;
        this.statisticsMonth = statisticsMonth;
        this.member = member;
        this.accommodation = accommodation;
    }

    public void setMonthlySum(int totalSales, int couponTotalSales, int downloadCount,
        int usedCount, int settlementAmount) {
        this.totalSales += totalSales;
        this.couponTotalSales += couponTotalSales;
        this.downloadCount += downloadCount;
        this.usedCount += usedCount;
        this.settlementAmount += settlementAmount;
    }

    public void setLocalCouponDownloadTop3(LocalCouponDownload localCouponDownload) {
        this.localCouponDownload = localCouponDownload;
    }
}
