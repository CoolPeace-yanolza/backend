package com.coolpeace.core.domain.statistics.entity;

import com.coolpeace.core.domain.accommodation.entity.Accommodation;
import com.coolpeace.core.domain.member.entity.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DailyStatistics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(value = 1)
    @Max(value = 31)
    private int statisticsDay;

    private int totalSales;

    private int couponTotalSales;

    private int downloadCount;

    private int usedCount;

    private int settlementAmount;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "accommodation_id")
    private Accommodation accommodation;

    public DailyStatistics(int statisticsDay, Member member, Accommodation accommodation) {
        this.statisticsDay = statisticsDay;
        this.member = member;
        this.accommodation = accommodation;
    }

    public void setSales(int totalSales, int couponTotalSales) {
        this.totalSales += totalSales;
        this.couponTotalSales += couponTotalSales;
    }

    public void setCoupon(int downloadCount, int usedCount) {
        this.downloadCount += downloadCount;
        this.usedCount += usedCount;
    }

    public void setSettlement(int settlementAmount) {
        this.settlementAmount += settlementAmount;
    }

    public DailyStatistics(Long id, int statisticsDay, int totalSales, int couponTotalSales,
        int downloadCount, int usedCount, int settlementAmount, Member member,
        Accommodation accommodation) {
        this.id = id;
        this.statisticsDay = statisticsDay;
        this.totalSales = totalSales;
        this.couponTotalSales = couponTotalSales;
        this.downloadCount = downloadCount;
        this.usedCount = usedCount;
        this.settlementAmount = settlementAmount;
        this.member = member;
        this.accommodation = accommodation;
    }
}
