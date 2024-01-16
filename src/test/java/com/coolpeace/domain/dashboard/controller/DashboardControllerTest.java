package com.coolpeace.domain.dashboard.controller;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.coolpeace.domain.dashboard.dto.response.CouponCountAvgResponse;
import com.coolpeace.global.util.RoomTestUtil;
import com.coolpeace.domain.accommodation.entity.Accommodation;
import com.coolpeace.domain.coupon.dto.response.CouponDailyResponse;
import com.coolpeace.domain.coupon.entity.Coupon;
import com.coolpeace.domain.coupon.entity.CouponDailyCondition;
import com.coolpeace.domain.coupon.service.CouponQueryService;
import com.coolpeace.domain.dashboard.dto.response.ByYearCumulativeDataResponse;
import com.coolpeace.domain.dashboard.dto.response.CumulativeDataResponse;
import com.coolpeace.domain.dashboard.dto.response.MonthlyCouponDownloadResponse;
import com.coolpeace.domain.dashboard.dto.response.MonthlyDataLightResponse;
import com.coolpeace.domain.dashboard.dto.response.MonthlyDataResponse;
import com.coolpeace.domain.dashboard.dto.response.WeeklyCouponResponse;
import com.coolpeace.domain.dashboard.service.DashboardService;
import com.coolpeace.domain.member.entity.Member;
import com.coolpeace.domain.room.entity.Room;
import com.coolpeace.domain.statistics.entity.DailyStatistics;
import com.coolpeace.domain.statistics.entity.LocalCouponDownload;
import com.coolpeace.domain.statistics.entity.MonthlyStatistics;
import com.coolpeace.global.builder.CouponTestBuilder;
import com.coolpeace.global.builder.MemberTestBuilder;
import com.coolpeace.global.builder.RoomTestBuilder;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(DashboardController.class)
class DashboardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DashboardService dashboardService;
    @MockBean
    private CouponQueryService couponQueryService;


    @Test
    @DisplayName("monthlyData()는 최근 6개월간 월간 통계 데이터를 내보낼 수 있다.")
    @WithMockUser
    void monthlyData_success() throws Exception {
        //given
        Member member = new MemberTestBuilder().encoded().build();
        Accommodation accommodation = new Accommodation(1L, "신라호텔", "주소주소", member);
        List<MonthlyStatistics> monthlyStatistics = getMonthlyStatistics(member, accommodation);
        List<MonthlyDataResponse> monthlyDataResponses = monthlyStatistics.stream()
            .map(MonthlyDataResponse::from).toList();

        given(dashboardService.monthlyData(any(), anyLong())).willReturn(monthlyDataResponses);
        //when,then
        mockMvc.perform(get("/v1/dashboards/{accommodation_id}/reports/month", 1L))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].statistics_year").isNumber())
            .andExpect(jsonPath("$[0].statistics_month").isNumber())
            .andExpect(jsonPath("$[0].total_sales").isNumber())
            .andExpect(jsonPath("$[0].coupon_total_sales").isNumber())
            .andExpect(jsonPath("$[0].download_count").isNumber())
            .andExpect(jsonPath("$[0].used_count").isNumber())
            .andExpect(jsonPath("$[0].settlement_amount").isNumber())
            .andExpect(jsonPath("$[0].conversion_rate").isNumber())
            .andExpect(jsonPath("$[1].statistics_year").isNumber())
            .andExpect(jsonPath("$[1].statistics_month").isNumber())
            .andExpect(jsonPath("$[1].total_sales").isNumber())
            .andExpect(jsonPath("$[1].coupon_total_sales").isNumber())
            .andExpect(jsonPath("$[1].download_count").isNumber())
            .andExpect(jsonPath("$[1].used_count").isNumber())
            .andExpect(jsonPath("$[1].settlement_amount").isNumber())
            .andExpect(jsonPath("$[1].conversion_rate").isNumber())
            .andExpect(jsonPath("$[2].statistics_year").isNumber())
            .andExpect(jsonPath("$[2].statistics_month").isNumber())
            .andExpect(jsonPath("$[2].total_sales").isNumber())
            .andExpect(jsonPath("$[2].coupon_total_sales").isNumber())
            .andExpect(jsonPath("$[2].download_count").isNumber())
            .andExpect(jsonPath("$[2].used_count").isNumber())
            .andExpect(jsonPath("$[2].settlement_amount").isNumber())
            .andExpect(jsonPath("$[2].conversion_rate").isNumber())
            .andDo(print());
    }

    @Test
    @DisplayName("weeklyCoupon()는 이번달 쿠폰에 대한 리포트를 내보낼 수 있다.")
    @WithMockUser
    void weeklyCoupon_success() throws Exception {
        //given
        Member member = new MemberTestBuilder().encoded().build();
        Accommodation accommodation = new Accommodation(1L, "신라호텔", "주소주소", member);
        DailyStatistics dailyStatistics1 = new DailyStatistics(1L, 1, 1000000,
            100000, 11, 10, 100000, member, accommodation);
        DailyStatistics dailyStatistics2 = new DailyStatistics(2L, 2, 1000000,
            100000, 11, 10, 100000, member, accommodation);
        List<DailyStatistics> dailyStatisticsList = new ArrayList<>();
        dailyStatisticsList.add(dailyStatistics1);
        dailyStatisticsList.add(dailyStatistics2);
        WeeklyCouponResponse weeklyCouponResponse = WeeklyCouponResponse.from(dailyStatisticsList);

        given(dashboardService.weeklyCoupon(any(), anyLong())).willReturn(weeklyCouponResponse);

        //when, then
        mockMvc.perform(get("/v1/dashboards/{accommodation_id}/reports/week", 1L))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.coupon_total_sales").isNumber())
            .andExpect(jsonPath("$.used_count").isNumber())
            .andExpect(jsonPath("$.settlement_amount").isNumber())
            .andExpect(jsonPath("$.download_count").isNumber())
            .andDo(print());
    }

    @Test
    @DisplayName("dailyCouponReport()는 일간 쿠폰 리포트를 내보낼 수 있다.")
    @WithMockUser
    void dailyCouponReport_success() throws Exception {
        //given
        Member member = new MemberTestBuilder().encoded().build();
        Accommodation accommodation = new Accommodation(1L, "신라호텔", "주소주소", member);
        List<Room> rooms = new RoomTestBuilder(accommodation).buildList();
        List<Room> randomRooms = RoomTestUtil.getRandomRooms(rooms);
        Coupon coupon1 = new CouponTestBuilder(accommodation, member, randomRooms).build();
        Coupon coupon2 = new CouponTestBuilder(accommodation, member, randomRooms).build();
        List<Coupon> couponList = new ArrayList<>();
        couponList.add(coupon1);
        couponList.add(coupon2);
        CouponDailyResponse couponDailyResponse = CouponDailyResponse.from
            (CouponDailyCondition.NO_EXPOSURE,
                couponList.stream().map(Coupon::getCouponTitle).toList());
        given(couponQueryService.dailyReport(any(), anyLong())).willReturn(couponDailyResponse);

        //when, then
        mockMvc.perform(get("/v1/dashboards/{accommodation_id}/reports/daily", 1L))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.condition").isString())
            .andExpect(jsonPath("$.coupon_titles[0]").isString())
            .andExpect(jsonPath("$.coupon_titles[1]").isString())
            .andDo(print());
    }

    @Test
    @DisplayName("downloadCouponTop3()는 지역별 다운로드 Top3를 내보낼 수 있다.")
    @WithMockUser
    void downloadCouponTop3_success() throws Exception {
        //given
        LocalCouponDownload couponDownload = new LocalCouponDownload(1L, "강남구",
            "재방문고객 20%할인", "첫방문고객 15000원할인", "모든고객 10000원할인");
        MonthlyCouponDownloadResponse monthlyCouponDownloadResponse =
            MonthlyCouponDownloadResponse.from(couponDownload);

        given(dashboardService.downloadCouponTop3(any(), anyLong()))
            .willReturn(monthlyCouponDownloadResponse);

        //when, then
        mockMvc.perform(get("/v1/dashboards/{accommodation_id}/coupons/local/download", 1L))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.first_coupon_title").isString())
            .andExpect(jsonPath("$.second_coupon_title").isString())
            .andExpect(jsonPath("$.third_coupon_title").isString())
            .andDo(print());
    }

    @Test
    @DisplayName("couponCountAvg()는 지역별 쿠폰 평균 갯수를 불러올 수 있다.")
    @WithMockUser
    void couponCountAvg_success() throws Exception {
        //given
        CouponCountAvgResponse couponCountAvgResponse =
            CouponCountAvgResponse.from(11, 2, "강남구");

        given(dashboardService.couponCountAvg(any(), anyLong())).willReturn(couponCountAvgResponse);

        //when, then
        mockMvc.perform(get("/v1/dashboards/{accommodation_id}/coupons/local/count", 1L))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.address").isString())
            .andExpect(jsonPath("$.coupon_avg").isString())
            .andDo(print());
    }

    @Test
    @DisplayName("byYearCumulativeData()는 연도별 누적현황을 내보낼 수 있다.")
    @WithMockUser
    void byYearCumulativeData_success() throws Exception {
        //given
        Member member = new MemberTestBuilder().encoded().build();
        Accommodation accommodation = new Accommodation(1L, "신라호텔", "주소주소", member);
        List<MonthlyStatistics> monthlyStatistics = getMonthlyStatistics(member, accommodation);

        ByYearCumulativeDataResponse from = ByYearCumulativeDataResponse.from
            (1000000000, 800000000, 45000,
                monthlyStatistics.stream().map(MonthlyDataLightResponse::from).toList());
        given(dashboardService.byYearCumulativeData(anyInt(),any(), anyLong())).willReturn(from);

        //when, then
        mockMvc.perform(get("/v1/dashboards/{accommodation_id}/reports/year", 1L)
                .queryParam("year","2023"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.coupon_total_sales").isNumber())
            .andExpect(jsonPath("$.coupon_use_sales").isNumber())
            .andExpect(jsonPath("$.coupon_total_used_count").isNumber())
            .andExpect(jsonPath("$.coupon_sales_list").exists())
            .andDo(print());
    }

    @Test
    @DisplayName("cumulativeData()는 총 누적 현황을 내보낼 수 있다.")
    @WithMockUser
    void cumulativeData_success() throws Exception {
        //given
        CumulativeDataResponse from = CumulativeDataResponse.from
            (1000000000, 800000000, 40000, 50000);

        given(dashboardService.cumulativeData(any(), anyLong())).willReturn(from);
        //when,then
        mockMvc.perform(get("/v1/dashboards/{accommodation_id}/reports/total", 1L))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.coupon_total_sales").isNumber())
            .andExpect(jsonPath("$.coupon_use_sales").isNumber())
            .andExpect(jsonPath("$.coupon_total_used_count").isNumber())
            .andExpect(jsonPath("$.coupon_total_download_count").isNumber())
            .andDo(print());

    }


    private List<MonthlyStatistics> getMonthlyStatistics(Member member,
        Accommodation accommodation) {
        MonthlyStatistics monthlyStatistics1 = new MonthlyStatistics(1L, 2023, 10, 10000000,
            9000000, 700, 500, 1000000, member, accommodation);
        MonthlyStatistics monthlyStatistics2 = new MonthlyStatistics(2L, 2023, 11, 20000000,
            9000000, 700, 500, 1000000, member, accommodation);
        MonthlyStatistics monthlyStatistics3 = new MonthlyStatistics(3L, 2023, 12, 30000000,
            9000000, 700, 500, 1000000, member, accommodation);

        List<MonthlyStatistics> monthlyStatisticsList = new ArrayList<>();
        monthlyStatisticsList.add(monthlyStatistics1);
        monthlyStatisticsList.add(monthlyStatistics2);
        monthlyStatisticsList.add(monthlyStatistics3);
        return monthlyStatisticsList;
    }

}
