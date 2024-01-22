package com.coolpeace.docs.settlement;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.coolpeace.domain.settlement.controller.SettlementController;
import com.coolpeace.domain.settlement.dto.request.SearchSettlementParams;
import com.coolpeace.domain.settlement.dto.response.SettlementResponse;
import com.coolpeace.domain.settlement.dto.response.SumSettlementResponse;
import com.coolpeace.domain.settlement.repository.OrderBy;
import com.coolpeace.domain.settlement.service.SettlementService;
import com.coolpeace.global.config.RestDocsUnitTest;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.epages.restdocs.apispec.SimpleType;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

@WebMvcTest(SettlementController.class)
class SettlementControllerTest extends RestDocsUnitTest {

    private static final String MOCK_AUTHORIZATION_HEADER = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJjbGllbnRfaWQiOiIyIiwiY2xpZW50X2VtYWlsIjoidGVzdEB0ZXN0LmNvbSIsImlzcyI6ImNvb2xwZWFjZSIsImlhdCI6MTcwNDYzODY5NSwiZXhwIjoxNzA0Njc0Njk1fQ.Re2QfSHVBJ9gKq8rCpv8wlvSuDS80jwEjHgGIxg840Og1ng6R_cozWOgw_eU9hgoYW0NUIAZLW322scfkUCRvQ";


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
        mockMvc.perform(RestDocumentationRequestBuilders.get("/v1/settlements/{accommodation_id}/summary",1)
                .header("Authorization", MOCK_AUTHORIZATION_HEADER))
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
        SettlementResponse settlementResponse1 = SettlementResponse
            .from(LocalDate.now(),"YC000011","크리스마스 쿠폰", 2,
                10000, 0, 0, 10000,
                LocalDate.now().plusMonths(1));
        SettlementResponse settlementResponse2 = SettlementResponse
            .from(LocalDate.now(),"YC000012","대박 할인 쿠폰", 2,
                20000, 1000, 0, 19000,
                LocalDate.now().plusMonths(1));

        List<SettlementResponse> settlementResponseList = new ArrayList<>();
        settlementResponseList.add(settlementResponse1);
        settlementResponseList.add(settlementResponse2);

        given(settlementService.searchSettlement(any(), anyLong(), any(SearchSettlementParams.class),
            anyInt(),anyInt())).willReturn(settlementResponseList);
        //when,then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/v1/settlements/{accommodation_id}", 1)
                .queryParam("start","2023-09-27")
                .queryParam("order", String.valueOf(OrderBy.COUPON_USE_DATE))
                .queryParam("end","2023-12-03")
                .header("Authorization", MOCK_AUTHORIZATION_HEADER))
            .andExpect(status().isOk())
            .andDo(document("settlement-search",
                resource(ResourceSnippetParameters.builder()
                    .tag("정산")
                    .description("지난 정산 조회 API")
                    .queryParameters(
                        parameterWithName("page").type(SimpleType.INTEGER).description("페이지 수").optional(),
                        parameterWithName("page_size").type(SimpleType.INTEGER).description("페이지 사이즈").optional(),
                        parameterWithName("start").type(SimpleType.STRING).description("검색 시작일"),
                        parameterWithName("end").type(SimpleType.STRING).description("검색 종료일"),
                        parameterWithName("order").type(SimpleType.STRING).description("정렬 기준")
                    )
                    .responseSchema(Schema.schema(SettlementResponse.class.getSimpleName()))
                    .responseFields(
                        fieldWithPath("[].coupon_use_date").type(JsonFieldType.STRING).description("쿠폰 사용일"),
                        fieldWithPath("[].coupon_number").type(JsonFieldType.STRING).description("쿠폰 번호"),
                        fieldWithPath("[].coupon_name").type(JsonFieldType.STRING).description("관리 쿠폰명"),
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
