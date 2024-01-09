package com.coolpeace.api.domain.settlement.repository;

import com.coolpeace.api.domain.accommodation.entity.Accommodation;
import com.coolpeace.api.domain.settlement.entity.Settlement;

import java.util.List;

public interface SettlementRepositoryCustom {
    List<Settlement> findAllByAccommodationForDailyUpdate(Accommodation accommodation);
}
