package com.coolpeace.core.domain.statistics.repository;

import com.coolpeace.core.domain.accommodation.entity.Accommodation;
import com.coolpeace.core.domain.statistics.entity.MonthlyStatistics;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MonthlyStatisticsRepository extends JpaRepository<MonthlyStatistics, Long> {

    Optional<MonthlyStatistics> findByAccommodationAndStatisticsYearAndStatisticsMonth(
            Accommodation accommodation, @Min(value = 2000) int statisticsYear,
            @Min(value = 1) @Max(value = 12) int statisticsMonth);

    List<MonthlyStatistics> findAllByAccommodationAndStatisticsYear(
        Accommodation accommodation, @Min(value = 2000) int statisticsYear);

    List<MonthlyStatistics> findAllByAccommodation(Accommodation accommodation);
    
}
