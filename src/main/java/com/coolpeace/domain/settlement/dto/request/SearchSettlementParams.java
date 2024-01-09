package com.coolpeace.domain.settlement.dto.request;


import com.coolpeace.domain.settlement.repository.OrderBy;

public record SearchSettlementParams(
    String startDate,
    String endDate,
    OrderBy orderBy
){

}
