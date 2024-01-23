package com.coolpeace.domain.dashboard.dto.response;

import java.util.List;

public record WrapMonthlyDataResponse (
    List<MonthlyDataResponse> monthlyDataResponses
){

    public static WrapMonthlyDataResponse from(List<MonthlyDataResponse> monthlyDataResponses) {
        return new WrapMonthlyDataResponse(monthlyDataResponses);
    }
}
