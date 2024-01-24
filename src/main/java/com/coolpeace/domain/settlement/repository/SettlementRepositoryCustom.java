package com.coolpeace.domain.settlement.repository;

import com.coolpeace.domain.accommodation.entity.Accommodation;
import com.coolpeace.domain.data.dto.request.SettlementStatistic;
import com.coolpeace.domain.settlement.entity.Settlement;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.Query;

public interface SettlementRepositoryCustom {
    List<Settlement> findAllByAccommodationForDailyUpdate(LocalDate localDate,Accommodation accommodation);


}
