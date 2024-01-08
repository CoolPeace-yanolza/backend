package com.coolpeace.domain.statistics.repository;

import com.coolpeace.domain.accommodation.entity.Accommodation;
import com.coolpeace.domain.statistics.entity.MonthlyStatistics;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MonthlyStatisticsRepository extends JpaRepository<MonthlyStatistics, Long> {

    Optional<MonthlyStatistics> findByAccommodationAndStatisticsYearAndStatisticsMonth(
        Accommodation accommodation, @Min(value = 2000) int statisticsYear,
        @Min(value = 1) @Max(value = 12) int statisticsMonth);

    
}
