package com.coolpeace.domain.statistics.repository;

import com.coolpeace.domain.statistics.entity.MonthlyStatistics;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MonthlyStatisticsRepository extends JpaRepository<MonthlyStatistics,Long> {

}
