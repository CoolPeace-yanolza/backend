package com.coolpeace.domain.statistics.repository;

import com.coolpeace.domain.member.entity.Member;
import com.coolpeace.domain.statistics.entity.DailyStatistics;
import java.util.Optional;
import javax.swing.text.html.Option;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyStatisticsRepository extends JpaRepository<DailyStatistics,Long> {

    Optional<DailyStatistics> findByMemberAndStatisticsDay(Member member, int statisticsDay);

}
