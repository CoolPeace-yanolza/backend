package com.coolpeace.domain.statistics.repository;

import com.coolpeace.domain.accommodation.entity.Accommodation;
import com.coolpeace.domain.member.entity.Member;
import com.coolpeace.domain.statistics.entity.DailyStatistics;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyStatisticsRepository extends JpaRepository<DailyStatistics,Long> {

    Optional<DailyStatistics> findByAccommodationAndStatisticsDay
        (Accommodation accommodation, int statisticsDay);

    List<DailyStatistics> findAllByMember(Member member);

}
