package com.coolpeace.core.domain.statistics.repository;

import com.coolpeace.core.domain.accommodation.entity.Accommodation;
import com.coolpeace.core.domain.statistics.entity.DailyStatistics;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DailyStatisticsRepository extends JpaRepository<DailyStatistics,Long> {

    Optional<DailyStatistics> findByAccommodationAndStatisticsDay
        (Accommodation accommodation, int statisticsDay);

    List<DailyStatistics> findAllByAccommodation(Accommodation accommodation);

}
