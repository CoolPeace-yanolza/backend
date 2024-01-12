package com.coolpeace.api.domain.dashboard.repository;


import com.coolpeace.core.domain.accommodation.entity.Accommodation;
import com.coolpeace.core.domain.accommodation.repository.AccommodationRepository;
import com.coolpeace.core.domain.member.entity.Member;
import com.coolpeace.core.domain.member.repository.MemberRepository;
import com.coolpeace.core.domain.statistics.entity.MonthlyStatistics;
import com.coolpeace.core.domain.statistics.repository.MonthlyStatisticsRepository;
import com.coolpeace.api.global.builder.MemberTestBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class DashboardRepositoryTest {

    @Autowired
    private MonthlyStatisticsRepository monthlyStatisticsRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private AccommodationRepository accommodationRepository;

    @Test
    @DisplayName("지난 6개월간의 월간 통계를 찾아낼 수 있다.")
    void findLast6monthsMonthlyStatistics_success(){
        Member member = new MemberTestBuilder().encoded().build();
        Accommodation accommodation = new Accommodation(1L, "신라호텔", "주소주소", member);
        memberRepository.save(member);
        accommodationRepository.save(accommodation);
        MonthlyStatistics monthlyStatistics1 = new MonthlyStatistics(1L, 2022, 12, 10000000,
            9000000, 700, 500, 1000000, member, accommodation);
        MonthlyStatistics monthlyStatistics2 = new MonthlyStatistics(2L, 2022, 11, 20000000,
            9000000, 700, 500, 1000000, member, accommodation);
        MonthlyStatistics monthlyStatistics3 = new MonthlyStatistics(3L, 2022, 10, 30000000,
            9000000, 700, 500, 1000000, member, accommodation);
        MonthlyStatistics monthlyStatistics4 = new MonthlyStatistics(4L, 2023, 4, 10000000,
            9000000, 700, 500, 1000000, member, accommodation);
        MonthlyStatistics monthlyStatistics5 = new MonthlyStatistics(5L, 2023, 6, 20000000,
            9000000, 700, 500, 1000000, member, accommodation);
        MonthlyStatistics monthlyStatistics6 = new MonthlyStatistics(6L, 2023, 7, 30000000,
            9000000, 700, 500, 1000000, member, accommodation);
        monthlyStatisticsRepository.save(monthlyStatistics2);
        monthlyStatisticsRepository.save(monthlyStatistics3);
        monthlyStatisticsRepository.save(monthlyStatistics1);
        monthlyStatisticsRepository.save(monthlyStatistics4);
        monthlyStatisticsRepository.save(monthlyStatistics5);
        monthlyStatisticsRepository.save(monthlyStatistics6);
        //when
        List<MonthlyStatistics> last6monthsMonthlyStatistics1 = monthlyStatisticsRepository.findLast6monthsMonthlyStatistics
            (accommodation, 2022, 11, 2023, 4);
        List<MonthlyStatistics> last6monthsMonthlyStatistics2 = monthlyStatisticsRepository.findLast6monthsMonthlyStatistics
            (accommodation, 2023, 1, 2023, 6);
        //then
        assertThat(last6monthsMonthlyStatistics1).size().isEqualTo(2);
        assertThat(last6monthsMonthlyStatistics2).size().isEqualTo(2);
    }
}
