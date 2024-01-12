package com.coolpeace.api.docs.dashboard;

import com.coolpeace.api.domain.coupon.dto.response.CouponDailyResponse;
import com.coolpeace.api.domain.coupon.service.CouponQueryService;
import com.coolpeace.api.domain.dashboard.dto.response.*;
import com.coolpeace.api.domain.dashboard.service.DashboardService;
import com.coolpeace.api.global.builder.CouponTestBuilder;
import com.coolpeace.api.global.builder.MemberTestBuilder;
import com.coolpeace.api.global.builder.RoomTestBuilder;
import com.coolpeace.api.global.common.RestDocsUnitTest;
import com.coolpeace.api.global.util.RoomTestUtil;
import com.coolpeace.core.domain.accommodation.entity.Accommodation;
import com.coolpeace.core.domain.coupon.entity.Coupon;
import com.coolpeace.core.domain.coupon.entity.CouponDailyCondition;
import com.coolpeace.core.domain.member.entity.Member;
import com.coolpeace.core.domain.room.entity.Room;
import com.coolpeace.core.domain.statistics.entity.DailyStatistics;
import com.coolpeace.core.domain.statistics.entity.LocalCouponDownload;
import com.coolpeace.core.domain.statistics.entity.MonthlyStatistics;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.epages.restdocs.apispec.SimpleType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.ArrayList;
import java.util.List;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DashboardControllerTest extends RestDocsUnitTest {

    @MockBean
    private DashboardService dashboardService;
    @MockBean
    private CouponQueryService couponQueryService;


    @Test
    @DisplayName("월간 비교 그래프 조회")
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
        mockMvc.perform(RestDocumentationRequestBuilders
                .get("/v1/dashboards/{accommodation_id}/reports/month", 1L))
            .andExpect(status().isOk())
            .andDo(document("dashboard-monthly",
                resource(ResourceSnippetParameters.builder()
                    .tag("대시보드")
                    .description("월간 비교 그래프 조회 API")
                    .responseSchema(Schema.schema(MonthlyDataResponse.class.getSimpleName()))
                    .responseFields(
                        fieldWithPath("[].statistics_year").type(JsonFieldType.NUMBER)
                            .description("통계 집계한 연도"),
                        fieldWithPath("[].statistics_month").type(JsonFieldType.NUMBER)
                            .description("통계 집계한 월"),
                        fieldWithPath("[].total_sales").type(JsonFieldType.NUMBER)
                            .description("해당 월 총 매출 "),
                        fieldWithPath("[].coupon_total_sales").type(JsonFieldType.NUMBER)
                            .description("쿠폰 적용 매출"),
                        fieldWithPath("[].download_count").type(JsonFieldType.NUMBER)
                            .description("쿠폰 다운로드 수"),
                        fieldWithPath("[].used_count").type(JsonFieldType.NUMBER)
                            .description("쿠폰 사용 완료 수 "),
                        fieldWithPath("[].settlement_amount").type(JsonFieldType.NUMBER)
                            .description("이번달 쿠폰 정산 금액"),
                        fieldWithPath("[].conversion_rate").type(JsonFieldType.NUMBER)
                            .description("전환율")
                    )
                    .build()
                )));
    }

    @Test
    @DisplayName("이번달 쿠폰 현황")
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
        mockMvc.perform(RestDocumentationRequestBuilders
                .get("/v1/dashboards/{accommodation_id}/reports/week", 1L))
            .andExpect(status().isOk())
            .andDo(document("dashboard-weekly",
                resource(ResourceSnippetParameters.builder()
                    .tag("대시보드")
                    .description("이번달 쿠폰 현황 API")
                    .responseSchema(Schema.schema(WeeklyCouponResponse.class.getSimpleName()))
                    .responseFields(
                        fieldWithPath("coupon_total_sales").type(JsonFieldType.NUMBER)
                            .description("쿠폰 사용 총 매출"),
                        fieldWithPath("used_count").type(JsonFieldType.NUMBER)
                            .description("쿠폰 사용한 예약 건수"),
                        fieldWithPath("settlement_amount").type(JsonFieldType.NUMBER)
                            .description("쿠폰 사용한 예약 건수 "),
                        fieldWithPath("download_count").type(JsonFieldType.NUMBER)
                            .description("쿠폰 다운로드 수")
                    )
                    .build()
                )));
    }

    @Test
    @DisplayName("일간 쿠폰 리포트")
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
            (CouponDailyCondition.EXPIRATION_3DAYS,
                couponList.stream().map(Coupon::getCouponTitle).toList());
        given(couponQueryService.dailyReport(any(), anyLong())).willReturn(couponDailyResponse);

        //when, then
        mockMvc.perform(RestDocumentationRequestBuilders
                .get("/v1/dashboards/{accommodation_id}/reports/daily", 1L))
            .andExpect(status().isOk())
            .andDo(document("dashboard-daily",
                resource(ResourceSnippetParameters.builder()
                    .tag("대시보드")
                    .description("일간 쿠폰 리포트 API")
                    .responseSchema(Schema.schema(CouponDailyResponse.class.getSimpleName()))
                    .responseFields(
                        fieldWithPath("condition").type(JsonFieldType.STRING)
                            .description("조건에 맞는 상태"),
                        fieldWithPath("coupon_titles[]").type(JsonFieldType.ARRAY)
                            .description("조건에 맞는 쿠폰 이름들")
                    )
                    .build()
                )));
    }

    @Test
    @DisplayName("지역별 쿠폰 다운로드 수 Top3")
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
        mockMvc.perform(RestDocumentationRequestBuilders
                .get("/v1/dashboards/{accommodation_id}/coupons/download", 1L))
            .andExpect(status().isOk())
            .andDo(document("dashboard-download",
                resource(ResourceSnippetParameters.builder()
                    .tag("대시보드")
                    .description("지역별 쿠폰 다운로드 수 Top3 API")
                    .responseSchema(Schema.schema(MonthlyCouponDownloadResponse.class.getSimpleName()))
                    .responseFields(
                        fieldWithPath("first_coupon_title").type(JsonFieldType.STRING)
                            .description("1위 쿠폰 이름"),
                        fieldWithPath("second_coupon_title").type(JsonFieldType.STRING)
                            .description("2위 쿠폰 이름"),
                        fieldWithPath("third_coupon_title").type(JsonFieldType.STRING)
                            .description("3위 쿠폰 이름")
                    )
                    .build()
                )));
    }
    @Test
    @DisplayName("연도별 누적 리포트")
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
        mockMvc.perform(RestDocumentationRequestBuilders
                .get("/v1/dashboards/{accommodation_id}/reports/year", 1L)
                .queryParam("year","2023"))
            .andExpect(status().isOk())
            .andDo(document("dashboard-year",
                resource(ResourceSnippetParameters.builder()
                    .tag("대시보드")
                    .description("연도별 누적 리포트 API")
                    .queryParameters(
                        parameterWithName("year").type(SimpleType.INTEGER).description("검색할 연도")
                    )
                    .responseSchema(Schema.schema(ByYearCumulativeDataResponse.class.getSimpleName()))
                    .responseFields(
                        fieldWithPath("coupon_total_sales").type(JsonFieldType.NUMBER)
                            .description("쿠폰 사용 총 매출"),
                        fieldWithPath("coupon_use_sales").type(JsonFieldType.NUMBER)
                            .description("누적 쿠폰 사용 금액"),
                        fieldWithPath("coupon_total_used_count").type(JsonFieldType.NUMBER)
                            .description("누적 쿠폰 사용 총 횟수"),
                        fieldWithPath("coupon_sales_list[]").type(JsonFieldType.ARRAY)
                            .description("월별 데이터"),
                        fieldWithPath("coupon_sales_list[].statistics_month").type(JsonFieldType.NUMBER)
                            .description("몇 월"),
                        fieldWithPath("coupon_sales_list[].total_sales").type(JsonFieldType.NUMBER)
                            .description("월별 전체 매출"),
                        fieldWithPath("coupon_sales_list[].coupon_total_sales").type(JsonFieldType.NUMBER)
                            .description("월별 쿠폰 적용 매출")
                    )
                    .build()
                )));
    }
    @Test
    @DisplayName("총 누적 쿠폰 현황")
    @WithMockUser
    void cumulativeData_success() throws Exception {
        //given
        CumulativeDataResponse from = CumulativeDataResponse.from
            (1000000000, 800000000, 40000, 50000);

        given(dashboardService.cumulativeData(any(), anyLong())).willReturn(from);
        //when,then
        mockMvc.perform(RestDocumentationRequestBuilders.
                get("/v1/dashboards/{accommodation_id}/reports/total", 1L))
            .andExpect(status().isOk())
            .andDo(document("dashboard-total",
                resource(ResourceSnippetParameters.builder()
                    .tag("대시보드")
                    .description("총 누적 쿠폰 현황 API")
                    .responseSchema(Schema.schema(CumulativeDataResponse.class.getSimpleName()))
                    .responseFields(
                        fieldWithPath("coupon_total_sales").type(JsonFieldType.NUMBER)
                            .description("쿠폰 사용 총 매출"),
                        fieldWithPath("coupon_use_sales").type(JsonFieldType.NUMBER)
                            .description("누적 쿠폰 사용 금액"),
                        fieldWithPath("coupon_total_used_count").type(JsonFieldType.NUMBER)
                            .description("누적 쿠폰 사용 총 횟수"),
                        fieldWithPath("coupon_total_download_count").type(JsonFieldType.NUMBER)
                            .description("누적 쿠폰 총 다운로드 수")

                    )
                    .build()
                )));


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
