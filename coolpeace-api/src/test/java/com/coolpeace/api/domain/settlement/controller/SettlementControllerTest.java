package com.coolpeace.api.domain.settlement.controller;

import com.coolpeace.api.domain.settlement.dto.request.SearchSettlementParams;
import com.coolpeace.api.domain.settlement.dto.response.SettlementResponse;
import com.coolpeace.api.domain.settlement.dto.response.SumSettlementResponse;
import com.coolpeace.api.domain.settlement.service.SettlementService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
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
        mockMvc.perform(get("/v1/settlements/{accommodation_id}", 1L))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].coupon_use_date").exists())
            .andExpect(jsonPath("$[0].coupon_count").isNumber())
            .andExpect(jsonPath("$[0].discount_price").isNumber())
            .andExpect(jsonPath("$[0].cancel_price").isNumber())
            .andExpect(jsonPath("$[0].supply_price").isNumber())
            .andExpect(jsonPath("$[0].sum_price").isNumber())
            .andExpect(jsonPath("$[0].complete_at").exists())
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