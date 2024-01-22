package com.coolpeace.domain.settlement.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.coolpeace.domain.settlement.dto.request.SearchSettlementParams;
import com.coolpeace.domain.settlement.dto.response.SettlementResponse;
import com.coolpeace.domain.settlement.dto.response.SumSettlementResponse;
import com.coolpeace.domain.settlement.repository.OrderBy;
import com.coolpeace.domain.settlement.service.SettlementService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(SettlementController.class)
class SettlementControllerTest  {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SettlementService settlementService;

    @Test
    @DisplayName("sumSettlement()는 정산 요약 조회를 할 수 있다.")
    @WithMockUser
    void sumSettlement_success() throws Exception {
        //given
        SumSettlementResponse settlementResponse =
            SumSettlementResponse.from(50000, 250000);
        given(settlementService.sumSettlement(any(), anyLong())).willReturn(settlementResponse);

        //when,then
        mockMvc.perform(get("/v1/settlements/{accommodation_id}/summary", 1L))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.this_month_settlement_amount").isNumber())
            .andExpect(jsonPath("$.last_month_settlement_amount").isNumber()).andDo(print());

        verify(settlementService, times(1)).sumSettlement(any(), anyLong());
    }

    @Test
    @DisplayName("searchSettlement()는 지난 정산들을 조회 할 수 있다.")
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
        mockMvc.perform(get("/v1/settlements/{accommodation_id}", 1L)
                .queryParam("start", "2023-09-27")
                .queryParam("order", String.valueOf(OrderBy.COUPON_USE_DATE))
                .queryParam("end", "2023-12-03"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].coupon_number").isString())
            .andExpect(jsonPath("$[0].coupon_name").isString())
            .andExpect(jsonPath("$[0].coupon_use_date").exists())
            .andExpect(jsonPath("$[0].coupon_count").isNumber())
            .andExpect(jsonPath("$[0].discount_price").isNumber())
            .andExpect(jsonPath("$[0].cancel_price").isNumber())
            .andExpect(jsonPath("$[0].supply_price").isNumber())
            .andExpect(jsonPath("$[0].sum_price").isNumber())
            .andExpect(jsonPath("$[0].complete_at").exists())
            .andExpect(jsonPath("$[1].coupon_number").isString())
            .andExpect(jsonPath("$[1].coupon_name").isString())
            .andExpect(jsonPath("$[1].coupon_use_date").exists())
            .andExpect(jsonPath("$[1].coupon_count").isNumber())
            .andExpect(jsonPath("$[1].discount_price").isNumber())
            .andExpect(jsonPath("$[1].cancel_price").isNumber())
            .andExpect(jsonPath("$[1].supply_price").isNumber())
            .andExpect(jsonPath("$[1].sum_price").isNumber())
            .andExpect(jsonPath("$[1].complete_at").exists())
            .andDo(print());
        verify(settlementService,times(1))
            .searchSettlement(any(), anyLong(), any(SearchSettlementParams.class), anyInt(),anyInt());
    }

}
