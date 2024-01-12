package com.coolpeace.core.domain.settlement.repository;

import com.coolpeace.core.domain.accommodation.entity.Accommodation;
import com.coolpeace.core.domain.settlement.entity.Settlement;

import java.util.List;

public interface SettlementRepositoryCustom {
    List<Settlement> findAllByAccommodationForDailyUpdate(Accommodation accommodation);
}
