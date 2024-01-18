package com.coolpeace.domain.dashboard.service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import com.coolpeace.domain.accommodation.entity.Accommodation;
import com.coolpeace.domain.accommodation.entity.type.AccommodationType;
import com.coolpeace.domain.accommodation.exception.AccommodationNotFoundException;
import com.coolpeace.domain.accommodation.exception.AccommodationNotMatchMemberException;
import com.coolpeace.domain.accommodation.repository.AccommodationRepository;
import com.coolpeace.domain.dashboard.dto.response.ByYearCumulativeDataResponse;
import com.coolpeace.domain.dashboard.dto.response.CouponCountAvgResponse;
import com.coolpeace.domain.dashboard.dto.response.CumulativeDataResponse;
import com.coolpeace.domain.dashboard.dto.response.MonthlyCouponDownloadResponse;
import com.coolpeace.domain.dashboard.dto.response.MonthlyDataResponse;
import com.coolpeace.domain.dashboard.dto.response.WeeklyCouponResponse;
import com.coolpeace.domain.member.entity.Member;
import com.coolpeace.domain.member.exception.MemberNotFoundException;
import com.coolpeace.domain.member.repository.MemberRepository;
import com.coolpeace.domain.statistics.entity.DailyStatistics;
import com.coolpeace.domain.statistics.entity.LocalCouponDownload;
import com.coolpeace.domain.statistics.entity.MonthlyStatistics;
import com.coolpeace.domain.statistics.repository.DailyStatisticsRepository;
import com.coolpeace.domain.statistics.repository.LocalCouponDownloadRepository;
import com.coolpeace.domain.statistics.repository.MonthlyStatisticsRepository;
import com.coolpeace.global.builder.AccommodationTestBuilder;
import com.coolpeace.global.builder.MemberTestBuilder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@ExtendWith(MockitoExtension.class)
class DashboardServiceTest {

    @InjectMocks
    private DashboardService dashboardService;
    @Mock
    private MonthlyStatisticsRepository monthlyStatisticsRepository;
    @Mock
    private DailyStatisticsRepository dailyStatisticsRepository;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private AccommodationRepository accommodationRepository;
    @Mock
    private LocalCouponDownloadRepository localCouponDownloadRepository;

    @Nested
    @DisplayName("checkAccommodationMatchMember()는")
    class checkAccommodationMatchMember {

        @Test
        @DisplayName("숙박을 반환한다.")
        void _success() {
            //given
            Member member = new MemberTestBuilder().encoded().build();
            Accommodation accommodation = new Accommodation(1L, "신라호텔", "주소주소", member);

            given(accommodationRepository.findById(anyLong())).willReturn(
                Optional.of(accommodation));
            given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));
            //when
            Accommodation getAccommodation =
                dashboardService.checkAccommodationMatchMember("1", 1L);
            //then
            assertThat(getAccommodation).extracting("id", "name", "address")
                .containsExactly(accommodation.getId(),
                    accommodation.getName(), accommodation.getAddress());
        }

        @Test
        @DisplayName("숙박 ID에 해당하지 않은 숙박이 존재하지 않아 실패한다.")
        void accommodation_not_found_fail() {
            //given
            Member member = new MemberTestBuilder().encoded().build();
            //when, then
            assertThatThrownBy(() -> dashboardService.checkAccommodationMatchMember("1", 1L))
                .isInstanceOf(AccommodationNotFoundException.class);
        }

        @Test
        @DisplayName("멤버 ID에 해당하지 않은 회원이 존재하지 않아 실패한다.")
        void member_not_found_fail() {
            //given
            Member member = new MemberTestBuilder().encoded().build();
            Accommodation accommodation =
                new Accommodation(1L, "신라호텔", "주소주소", member);

            given(accommodationRepository.findById(anyLong())).willReturn(
                Optional.of(accommodation));
            //when, then
            assertThatThrownBy(() -> dashboardService.checkAccommodationMatchMember("1", 1L))
                .isInstanceOf(MemberNotFoundException.class);
        }

        @Test
        @DisplayName("숙박에 등록된 회원과 로그인한 회원이 달라 실패한다.")
        void not_match_fail() {
            //given
            Member member1 = new MemberTestBuilder().encoded().build();
            Member member2 = new MemberTestBuilder().encoded().build();
            Accommodation accommodation =
                new Accommodation(1L, "신라호텔", "주소주소", member1);

            given(accommodationRepository.findById(anyLong())).willReturn(
                Optional.of(accommodation));
            given(memberRepository.findById(anyLong())).willReturn(Optional.of(member2));
            //when, then
            assertThatThrownBy(() -> dashboardService.checkAccommodationMatchMember("1", 1L))
                .isInstanceOf(AccommodationNotMatchMemberException.class);
        }
    }

    @Test
    @DisplayName("monthlyData()는 최근 6개월간 월간 통계 데이터를 내보낼 수 있다.")
    void monthlyData_success() {
        //given
        Member member = new MemberTestBuilder().encoded().build();
        Accommodation accommodation = new Accommodation(1L, "신라호텔", "주소주소", member);
        List<MonthlyStatistics> monthlyStatisticsList = getMonthlyStatistics(
            member, accommodation);

        given(accommodationRepository.findById(anyLong())).willReturn(
            Optional.of(accommodation));
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));
        given(monthlyStatisticsRepository.findLast6monthsMonthlyStatistics(any(Accommodation.class),
            anyInt(), anyInt(), anyInt(), anyInt())).willReturn(monthlyStatisticsList);

        //when
        List<MonthlyDataResponse> monthlyDataResponses = dashboardService.monthlyData("1", 1L);

        //then
        assertThat(monthlyDataResponses).hasSize(3);
        assertThat(monthlyDataResponses.get(0)).extracting("totalSales", "statisticsMonth","conversionRate")
            .containsExactly(10000000, 12,71);
        assertThat(monthlyDataResponses.get(1)).extracting("totalSales", "statisticsMonth","conversionRate")
            .containsExactly(20000000, 11,71);
        assertThat(monthlyDataResponses.get(2)).extracting("totalSales", "statisticsMonth","conversionRate")
            .containsExactly(30000000, 10,71);

    }


    @Test
    @DisplayName("weeklyCoupon()는 이번달 쿠폰에 대한 리포트를 내보낼 수 있다.")
    void weeklyCoupon_success() {
        //given
        Member member = new MemberTestBuilder().encoded().build();
        Accommodation accommodation = new Accommodation(1L, "신라호텔", "주소주소", member);
        DailyStatistics dailyStatistics1 = new DailyStatistics(1L, 1, 1000000,
            100000, 11, 10, 100000, member, accommodation);
        DailyStatistics dailyStatistics2 = new DailyStatistics(1L, 2, 1000000,
            100000, 11, 10, 100000, member, accommodation);
        List<DailyStatistics> dailyStatisticsList = new ArrayList<>();
        dailyStatisticsList.add(dailyStatistics1);
        dailyStatisticsList.add(dailyStatistics2);

        given(accommodationRepository.findById(anyLong())).willReturn(
            Optional.of(accommodation));
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));
        given(dailyStatisticsRepository.findAllByAccommodation(any(Accommodation.class)))
            .willReturn(dailyStatisticsList);

        //when
        WeeklyCouponResponse weeklyCouponResponse =
            dashboardService.weeklyCoupon("1", 1L);
        //then
        assertThat(weeklyCouponResponse)
            .extracting("couponTotalSales", "usedCount", "settlementAmount","downloadCount")
            .containsExactly(200000, 20, 200000,22);
    }

    @Test
    @DisplayName("downloadCouponTop3()는 지역에서 다운로드가 가장 많은 쿠폰 이름들을 불러 올 수 있다.")
    void downloadCouponTop3_success() {
        //given
        Member member = new MemberTestBuilder().encoded().build();
        Accommodation accommodation = new Accommodation(1L, "신라호텔", "주소주소", member);
        LocalCouponDownload couponDownload = new LocalCouponDownload
            (1L, "강남구", "재방문고객 20%할인", "첫방문고객 15000원할인", "모든고객 10000원할인");
        MonthlyStatistics monthlyStatistics = new MonthlyStatistics(1L, 2023, 12, 10000000,
            9000000, 1000, 300, 1000000, member, accommodation);
        monthlyStatistics.setLocalCouponDownloadTop3(couponDownload);

        given(accommodationRepository.findById(anyLong())).willReturn(
            Optional.of(accommodation));
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));
        given(monthlyStatisticsRepository.findByAccommodationAndStatisticsYearAndStatisticsMonth
            (any(Accommodation.class), anyInt(),anyInt()))
            .willReturn(Optional.of(monthlyStatistics));

        //when
        MonthlyCouponDownloadResponse monthlyCouponDownloadResponse = dashboardService.downloadCouponTop3(
            "1", 1L);
        //then
        assertThat(monthlyCouponDownloadResponse)
            .extracting("firstCouponTitle", "secondCouponTitle", "thirdCouponTitle")
            .containsExactly("재방문고객 20%할인", "첫방문고객 15000원할인", "모든고객 10000원할인");
    }

    @Test
    @DisplayName("couponCountAvg()는 지역별 쿠폰 평균 갯수를 불러올 수 있다.")
    void couponCountAvg_success() {
        //given
        Member member = new MemberTestBuilder().encoded().build();
        Accommodation accommodation = new AccommodationTestBuilder(member).build();
        accommodation.setType(AccommodationType.MOTEL);
        String name = accommodation.getSigungu().getName();
        LocalCouponDownload localCouponDownload = new LocalCouponDownload(name,2023,12);
        localCouponDownload.setCount(5, AccommodationType.MOTEL);
        localCouponDownload.setCount(6, AccommodationType.MOTEL);
        given(localCouponDownloadRepository.findByRegionAndStatisticsYearAndStatisticsMonth(any(),anyInt(),anyInt()))
            .willReturn(Optional.of(localCouponDownload));
        given(accommodationRepository.findById(anyLong())).willReturn(
            Optional.of(accommodation));
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));
        //when
        CouponCountAvgResponse couponCountAvgResponse =
            dashboardService.couponCountAvg("1", 1L);

        //then
        assertThat(couponCountAvgResponse).extracting("address", "couponAvg")
            .containsExactly(name, "5.5");
    }


    @Test
    @DisplayName("byYearCumulativeData()는 연도별 누적 데이터를 내보낼 수 있다.")
    void byYearCumulativeData_success(){
        //given
        Member member = new MemberTestBuilder().encoded().build();
        Accommodation accommodation = new Accommodation(1L, "신라호텔", "주소주소", member);

        given(accommodationRepository.findById(anyLong())).willReturn(
            Optional.of(accommodation));
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));
        given(monthlyStatisticsRepository.findAllByAccommodationAndStatisticsYear
            (any(Accommodation.class), anyInt())).willReturn(getMonthlyStatistics(member,accommodation));
        //when
        ByYearCumulativeDataResponse byYearCumulativeDataResponse = dashboardService.byYearCumulativeData(
            2023,"1", 1L);
        //then
        assertThat(byYearCumulativeDataResponse).extracting
                ("couponTotalSales", "couponUseSales", "couponTotalUsedCount")
            .containsExactly(27000000, 3000000, 1500);
        assertThat(byYearCumulativeDataResponse.couponSalesList()).hasSize(3);
    }
    @Test
    @DisplayName("byYearCumulativeData()는 월별 데이터가 없으면 0을 반환한다.")
    void byYearCumulativeData_returnZero(){
        //given
        Member member = new MemberTestBuilder().encoded().build();
        Accommodation accommodation = new Accommodation(1L, "신라호텔", "주소주소", member);

        given(accommodationRepository.findById(anyLong())).willReturn(
            Optional.of(accommodation));
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));
        given(monthlyStatisticsRepository.findAllByAccommodationAndStatisticsYear
            (any(Accommodation.class), anyInt())).willReturn(Collections.emptyList());
        //when
        ByYearCumulativeDataResponse byYearCumulativeDataResponse = dashboardService.byYearCumulativeData(
            2023,"1", 1L);
        //then
        assertThat(byYearCumulativeDataResponse).extracting
                ("couponTotalSales", "couponUseSales", "couponTotalUsedCount")
            .containsExactly(0, 0, 0);
        assertThat(byYearCumulativeDataResponse.couponSalesList()).isEmpty();
    }

    @Test
    @DisplayName("cumulativeData()는 총 누적 데이터를 내보낼 수 있다.")
    void cumulativeData_success(){
        //given
        Member member = new MemberTestBuilder().encoded().build();
        Accommodation accommodation = new Accommodation(1L, "신라호텔", "주소주소", member);

        given(accommodationRepository.findById(anyLong())).willReturn(
            Optional.of(accommodation));
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));
        given(monthlyStatisticsRepository
            .findAllByAccommodation(any(Accommodation.class)))
            .willReturn(getMonthlyStatistics(member, accommodation));
        //when
        CumulativeDataResponse cumulativeData = dashboardService.cumulativeData(
            "1", 1L);
        //then
        assertThat(cumulativeData).extracting
                ("couponTotalSales", "couponUseSales", "couponTotalUsedCount","couponTotalDownloadCount")
            .containsExactly(27000000, 3000000, 1500, 2100);
    }

    private  List<MonthlyStatistics> getMonthlyStatistics(Member member,
        Accommodation accommodation) {
        MonthlyStatistics monthlyStatistics1 = new MonthlyStatistics(1L, 2023, 12, 10000000,
            9000000, 700, 500, 1000000, member, accommodation);
        MonthlyStatistics monthlyStatistics2 = new MonthlyStatistics(2L, 2023, 11, 20000000,
            9000000, 700, 500, 1000000, member, accommodation);
        MonthlyStatistics monthlyStatistics3 = new MonthlyStatistics(3L, 2023, 10, 30000000,
            9000000, 700, 500, 1000000, member, accommodation);

        List<MonthlyStatistics> monthlyStatisticsList = new ArrayList<>();
        monthlyStatisticsList.add(monthlyStatistics1);
        monthlyStatisticsList.add(monthlyStatistics2);
        monthlyStatisticsList.add(monthlyStatistics3);
        return monthlyStatisticsList;
    }



}
