package com.coolpeace.domain.statistics.entity;

import com.coolpeace.domain.accommodation.entity.Accommodation;
import com.coolpeace.domain.member.entity.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
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

    @Size(min = 1, max = 12)
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

    private String firstCouponTitle;

    private String secondCouponTitle;

    private String thirdCouponTitle;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "accommodation_id")
    private Accommodation accommodation;

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
}
