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

    public List<MonthlyDataResponse> monthlyData(String memberId, Long accommodationId) {
        Accommodation accommodation = checkAccommodationMatchMember(memberId, accommodationId);

        int[] last6Months = findLast6Months(LocalDate.now().getYear(),
            LocalDate.now().getMonth().getValue());
        List<MonthlyStatistics> last6monthsMonthlyStatistics =
            monthlyStatisticsRepository.findLast6monthsMonthlyStatistics
            (accommodation, last6Months[0], last6Months[1], last6Months[2], last6Months[3]);

        return last6monthsMonthlyStatistics.stream()
            .map(MonthlyDataResponse::from).toList();

    }

    public WeeklyCouponResponse weeklyCoupon(String memberId, Long accommodationId) {
        Accommodation accommodation = checkAccommodationMatchMember(memberId, accommodationId);

        List<DailyStatistics> dailyStatisticsList = dailyStatisticsRepository
            .findAllByAccommodation(accommodation);

        return WeeklyCouponResponse.from(dailyStatisticsList);
    }

    public MonthlyCouponDownloadResponse downloadCouponTop3(String memberId, Long accommodationId) {
        Accommodation accommodation = checkAccommodationMatchMember(memberId, accommodationId);

        MonthlyStatistics monthlyStatistics = monthlyStatisticsRepository
            .findByAccommodationAndStatisticsYearAndStatisticsMonth(accommodation,
                LocalDateTime.now().getYear(), LocalDateTime.now().getMonth().getValue())
            .orElseThrow(MonthlyStatisticsNotFoundException::new);

        return MonthlyCouponDownloadResponse.from(monthlyStatistics.getLocalCouponDownload());

    }

    public ByYearCumulativeDataResponse byYearCumulativeData(int year, String memberId, Long accommodationId) {
        Accommodation accommodation = checkAccommodationMatchMember(memberId, accommodationId);
        List<MonthlyStatistics> monthlyStatisticsList = monthlyStatisticsRepository
            .findAllByAccommodationAndStatisticsYear(accommodation, year);

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


    public CumulativeDataResponse cumulativeData(String memberId, Long accommodationId) {
        Accommodation accommodation = checkAccommodationMatchMember(memberId, accommodationId);
        List<MonthlyStatistics> monthlyStatisticsList = monthlyStatisticsRepository
            .findAllByAccommodation(accommodation);

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

    public Accommodation checkAccommodationMatchMember(String memberId, Long accommodationId) {
        Accommodation accommodation = accommodationRepository.findById(accommodationId)
            .orElseThrow(AccommodationNotFoundException::new);
        Member member = memberRepository.findById(Long.valueOf(memberId))
            .orElseThrow(MemberNotFoundException::new);
        if (!accommodation.getMember().equals(member)) {
            throw new AccommodationNotMatchMemberException();
        }
        return accommodation;
    }

    public int[] findLast6Months(int year, int month) {
        if (month >= 7) return new int[]{year, month - 6, year, month - 1};
        if (month == 1) return new int[]{year - 1, 7, year - 1, 12};
        return new int[]{year - 1, 6 + month, year, month - 1};
    }
}
