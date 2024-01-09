package com.coolpeace.api.domain.statistics.repository;

import com.coolpeace.api.domain.accommodation.entity.Accommodation;
import com.coolpeace.api.domain.statistics.entity.DailyStatistics;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyStatisticsRepository extends JpaRepository<DailyStatistics,Long> {

    Optional<DailyStatistics> findByAccommodationAndStatisticsDay
        (Accommodation accommodation, int statisticsDay);

    List<DailyStatistics> findAllByAccommodation(Accommodation accommodation);

}
