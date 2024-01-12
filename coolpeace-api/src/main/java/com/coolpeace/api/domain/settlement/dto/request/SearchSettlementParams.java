package com.coolpeace.api.domain.settlement.dto.request;


import com.coolpeace.core.domain.settlement.repository.OrderBy;

public record SearchSettlementParams(
    String startDate,
    String endDate,
    OrderBy orderBy
){

}
