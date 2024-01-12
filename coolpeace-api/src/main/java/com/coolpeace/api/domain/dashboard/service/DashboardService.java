package com.coolpeace.api.domain.dashboard.service;


import com.coolpeace.core.domain.accommodation.entity.Accommodation;
import com.coolpeace.core.domain.accommodation.repository.AccommodationRepository;
import com.coolpeace.api.domain.dashboard.dto.response.MonthlyDataLightResponse;
import com.coolpeace.api.domain.dashboard.dto.response.MonthlyDataResponse;
import com.coolpeace.api.domain.dashboard.dto.response.WeeklyCouponResponse;
import com.coolpeace.core.domain.member.entity.Member;
import com.coolpeace.core.domain.member.repository.MemberRepository;
import com.coolpeace.core.domain.statistics.exception.DailyStatisticsNotFoundException;
import com.coolpeace.core.domain.statistics.exception.MonthlyStatisticsNotFoundException;
import com.coolpeace.core.domain.statistics.repository.DailyStatisticsRepository;
import com.coolpeace.core.domain.statistics.repository.MonthlyStatisticsRepository;
import com.coolpeace.api.domain.accommodation.exception.AccommodationNotFoundException;
import com.coolpeace.api.domain.accommodation.exception.AccommodationNotMatchMemberException;
import com.coolpeace.api.domain.dashboard.dto.response.ByYearCumulativeDataResponse;
import com.coolpeace.api.domain.dashboard.dto.response.CumulativeDataResponse;
import com.coolpeace.api.domain.dashboard.dto.response.MonthlyCouponDownloadResponse;
import com.coolpeace.api.domain.member.exception.MemberNotFoundException;
import com.coolpeace.core.domain.statistics.entity.DailyStatistics;
import com.coolpeace.core.domain.statistics.entity.MonthlyStatistics;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DashboardService {

    private final MonthlyStatisticsRepository monthlyStatisticsRepository;
    private final DailyStatisticsRepository dailyStatisticsRepository;
    private final MemberRepository memberRepository;
    private final AccommodationRepository accommodationRepository;

    public MonthlyDataResponse monthlyData(Long memberId, Long accommodationId) {
        Accommodation accommodation = checkAccommodationMatchMember(memberId, accommodationId);

        MonthlyStatistics monthlyStatistics = monthlyStatisticsRepository
            .findByAccommodationAndStatisticsYearAndStatisticsMonth(accommodation,
                LocalDateTime.now().getYear(), LocalDateTime.now().getMonth().getValue())
            .orElseThrow(MonthlyStatisticsNotFoundException::new);

        return MonthlyDataResponse.from(monthlyStatistics);
    }

    public WeeklyCouponResponse weeklyCoupon(Long memberId, Long accommodationId) {
        Accommodation accommodation = checkAccommodationMatchMember(memberId, accommodationId);

        DailyStatistics dailyStatistics = dailyStatisticsRepository
            .findByAccommodationAndStatisticsDay(accommodation, LocalDateTime.now().getDayOfMonth())
            .orElseThrow(DailyStatisticsNotFoundException::new);

        return WeeklyCouponResponse.from(dailyStatistics);
    }

    public MonthlyCouponDownloadResponse downloadCouponTop3(Long memberId, Long accommodationId) {
        Accommodation accommodation = checkAccommodationMatchMember(memberId, accommodationId);

        MonthlyStatistics monthlyStatistics = monthlyStatisticsRepository
            .findByAccommodationAndStatisticsYearAndStatisticsMonth(accommodation,
                LocalDateTime.now().getYear(), LocalDateTime.now().getMonth().getValue())
            .orElseThrow(MonthlyStatisticsNotFoundException::new);

        return MonthlyCouponDownloadResponse.from(monthlyStatistics.getLocalCouponDownload());

    }

    public ByYearCumulativeDataResponse byYearCumulativeData(Long memberId, Long accommodationId) {
        Accommodation accommodation = checkAccommodationMatchMember(memberId, accommodationId);
        List<MonthlyStatistics> monthlyStatisticsList = monthlyStatisticsRepository
            .findAllByAccommodationAndStatisticsYear(accommodation, LocalDate.now().getYear());

        if (monthlyStatisticsList.isEmpty()) {
            throw new MonthlyStatisticsNotFoundException();
        }

        int couponTotalSales = 0;
        int couponUseSales = 0;
        int couponTotalUsedCount = 0;

        for (MonthlyStatistics monthlyStatistics : monthlyStatisticsList) {
            couponTotalSales += monthlyStatistics.getCouponTotalSales();
            couponUseSales += monthlyStatistics.getSettlementAmount();
            couponTotalUsedCount += monthlyStatistics.getUsedCount();
        }
        return new ByYearCumulativeDataResponse(couponTotalSales, couponUseSales,
            couponTotalUsedCount, monthlyStatisticsList.stream()
            .map(MonthlyDataLightResponse::from).toList());
    }


    public CumulativeDataResponse cumulativeData(Long memberId, Long accommodationId) {
        Accommodation accommodation = checkAccommodationMatchMember(memberId, accommodationId);
        List<MonthlyStatistics> monthlyStatisticsList = monthlyStatisticsRepository
            .findAllByAccommodation(accommodation);

        if (monthlyStatisticsList.isEmpty()) {
            throw new MonthlyStatisticsNotFoundException();
        }

        int couponTotalSales = 0;
        int couponUseSales = 0;
        int couponTotalUsedCount = 0;
        int couponTotalDownloadCount = 0;

        for (MonthlyStatistics monthlyStatistics : monthlyStatisticsList) {
            couponTotalSales += monthlyStatistics.getCouponTotalSales();
            couponUseSales += monthlyStatistics.getSettlementAmount();
            couponTotalUsedCount += monthlyStatistics.getUsedCount();
            couponTotalDownloadCount += monthlyStatistics.getDownloadCount();
        }
        return CumulativeDataResponse
            .from(couponTotalSales,couponUseSales,couponTotalUsedCount,couponTotalDownloadCount);
    }


    public Accommodation checkAccommodationMatchMember(Long memberId, Long accommodationId) {
        Accommodation accommodation = accommodationRepository.findById(accommodationId)
            .orElseThrow(AccommodationNotFoundException::new);
        Member member = memberRepository.findById(memberId)
            .orElseThrow(MemberNotFoundException::new);
        if (!accommodation.getMember().equals(member)) {
            throw new AccommodationNotMatchMemberException();
        }
        return accommodation;
    }

}
