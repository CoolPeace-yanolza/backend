package com.coolpeace.domain.settlement.repository;

import com.coolpeace.domain.accommodation.entity.Accommodation;
import com.coolpeace.domain.settlement.entity.Settlement;
import java.time.LocalDate;
import java.util.List;

public interface SettlementRepositoryCustom {
    List<Settlement> findAllByAccommodationForDailyUpdate(LocalDate localDate,Accommodation accommodation);
}
