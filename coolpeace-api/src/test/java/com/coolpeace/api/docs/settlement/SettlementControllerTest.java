package com.coolpeace.api.docs.settlement;

import com.coolpeace.api.domain.settlement.dto.request.SearchSettlementParams;
import com.coolpeace.api.domain.settlement.dto.response.SettlementResponse;
import com.coolpeace.api.domain.settlement.dto.response.SumSettlementResponse;
import com.coolpeace.api.domain.settlement.service.SettlementService;
import com.coolpeace.api.global.common.RestDocsUnitTest;
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

import java.time.LocalDate;
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
class SettlementControllerTest extends RestDocsUnitTest {

    @MockBean
    private SettlementService settlementService;


    @Test
    @DisplayName("정산 요약 조회")
    @WithMockUser
    void sumSettlement_success() throws Exception {
        //given
        SumSettlementResponse settlementResponse =
            SumSettlementResponse.from(50000, 250000);
        given(settlementService.sumSettlement(any(), anyLong())).willReturn(settlementResponse);

        //when,then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/v1/settlements/{accommodation_id}/summary",1))
            .andExpect(status().isOk())
            .andDo(document("settlement-summary",
                resource(ResourceSnippetParameters.builder()
                    .tag("정산")
                    .description("정산 요약 조회 API")
                    .responseSchema(Schema.schema(SumSettlementResponse.class.getSimpleName()))
                    .responseFields(
                        fieldWithPath("this_month_settlement_amount").type(JsonFieldType.NUMBER).description("오늘까지 정산 내역"),
                        fieldWithPath("last_month_settlement_amount").type(JsonFieldType.NUMBER).description("지난달 정산 내역")
                    )
                    .build()
                )));
    }

    @Test
    @DisplayName("지난 정산 조회")
    @WithMockUser
    void searchSettlement_success() throws Exception {
        //given
        SettlementResponse settlementResponse1 = SettlementResponse.from(LocalDate.now(), 2,
            10000, 0, 0, 10000,
            LocalDate.now().plusMonths(1));
        SettlementResponse settlementResponse2 = SettlementResponse.from(LocalDate.now(), 2,
            20000, 1000, 0, 19000,
            LocalDate.now().plusMonths(1));
        List<SettlementResponse> settlementResponseList = new ArrayList<>();
        settlementResponseList.add(settlementResponse1);
        settlementResponseList.add(settlementResponse2);

        given(settlementService.searchSettlement(any(), anyLong(), any(SearchSettlementParams.class),
            anyInt(),anyInt())).willReturn(settlementResponseList);
        //when,then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/v1/settlements/{accommodation_id}", 1)
                .queryParam("end_date","2023-09-27")
                .queryParam("order_by","coupon_use_date")
                .queryParam("start_date","2023-12-03"))
            .andExpect(status().isOk())
            .andDo(document("settlement-search",
                resource(ResourceSnippetParameters.builder()
                    .tag("정산")
                    .description("지난 정산 조회 API")
                    .queryParameters(
                        parameterWithName("page").type(SimpleType.INTEGER).description("페이지 수").optional(),
                        parameterWithName("page_size").type(SimpleType.INTEGER).description("페이지 사이즈").optional(),
                        parameterWithName("start_date").type(SimpleType.STRING).description("검색 시작일"),
                        parameterWithName("end_date").type(SimpleType.STRING).description("검색 종료일"),
                        parameterWithName("order_by").type(SimpleType.STRING).description("정렬 기준")
                    )
                    .responseSchema(Schema.schema(SettlementResponse.class.getSimpleName()))
                    .responseFields(
                        fieldWithPath("[].coupon_use_date").type(JsonFieldType.STRING).description("쿠폰 사용일"),
                        fieldWithPath("[].coupon_count").type(JsonFieldType.NUMBER).description("사용건수"),
                        fieldWithPath("[].discount_price").type(JsonFieldType.NUMBER).description("쿠폰할인금액"),
                        fieldWithPath("[].cancel_price").type(JsonFieldType.NUMBER).description("쿠폰취소금액"),
                        fieldWithPath("[].supply_price").type(JsonFieldType.NUMBER).description("오늘까지 정산 내역"),
                        fieldWithPath("[].sum_price").type(JsonFieldType.NUMBER).description("지원 금액"),
                        fieldWithPath("[].complete_at").type(JsonFieldType.STRING).description("정산 완료일")
                    )
                    .build()
                )));

    }

}
