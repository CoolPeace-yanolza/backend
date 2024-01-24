package com.coolpeace.domain.data.dto.request;

import java.time.LocalDate;

public record GenerateSettlementRequset (
    LocalDate start,
    LocalDate end
){



}
