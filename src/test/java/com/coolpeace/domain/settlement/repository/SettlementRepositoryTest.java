package com.coolpeace.domain.settlement.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.coolpeace.domain.accommodation.entity.Accommodation;
import com.coolpeace.domain.accommodation.repository.AccommodationRepository;
import com.coolpeace.domain.member.entity.Member;
import com.coolpeace.domain.member.repository.MemberRepository;
import com.coolpeace.domain.settlement.dto.response.SettlementResponse;
import com.coolpeace.domain.settlement.entity.Settlement;
import com.coolpeace.domain.statistics.entity.MonthlyStatistics;
import com.coolpeace.domain.statistics.repository.MonthlyStatisticsRepository;
import com.coolpeace.global.builder.MemberTestBuilder;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class SettlementRepositoryTest {

    @Autowired
    private MonthlyStatisticsRepository monthlyStatisticsRepository;

    @Autowired
    private SettlementRepository settlementRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private AccommodationRepository accommodationRepository;

    @Test
    @DisplayName("숙소,연도,월을 통해 월별 통계를 찾아낼 수 있다.")
    void findByAccommodationAndStatisticsYearAndStatisticsMonth() {
        //given
        Member member = new MemberTestBuilder().encoded().build();
        Accommodation accommodation = new Accommodation(1L, "신라호텔", "주소주소", member);
        MonthlyStatistics monthlyStatistics = new MonthlyStatistics(1L, 2023, 12, 10000000,
            9000000, 1000, 300, 1000000, member, accommodation);

        memberRepository.save(member);
        accommodationRepository.save(accommodation);
        monthlyStatisticsRepository.save(monthlyStatistics);
        //when
        MonthlyStatistics getMonthlyStatistics = monthlyStatisticsRepository.findByAccommodationAndStatisticsYearAndStatisticsMonth(
            accommodation, 2023, 12).get();

        //then
        assertThat(getMonthlyStatistics).extracting( "statisticsYear",
                "statisticsMonth", "totalSales", "couponTotalSales",
                "downloadCount", "usedCount", "settlementAmount")
            .containsExactly( 2023, 12, 10000000, 9000000, 1000, 300, 1000000);

    }
    @Test
    @DisplayName("페이징과 시작일 종료일을 통해 지난 정산들을 찾아낼 수 있다.")
    void findAllByAccommodationAndCouponUseDateAfterAndCouponUseDateBeforeOrderByCouponUseDate(){
        //given
        Member member = new MemberTestBuilder().encoded().build();
        Accommodation accommodation = new Accommodation(1L, "신라호텔", "주소주소", member);
        Settlement settlement1 = new Settlement(1L, LocalDate.of(2023, 10, 27), 10, 1000,
            0, 0, 1000, LocalDate.of(2023, 11, 10),accommodation);
        Settlement settlement2 = new Settlement(2L, LocalDate.of(2023, 12, 27), 10, 1000,
            0, 0, 1000, LocalDate.of(2024, 1, 10),accommodation);
        Settlement settlement3 = new Settlement(3L, LocalDate.of(2023, 8, 27), 10, 1000,
            0, 0, 1000, LocalDate.of(2023, 9, 10),accommodation);
        memberRepository.save(member);
        accommodationRepository.save(accommodation);
        settlementRepository.save(settlement1);
        settlementRepository.save(settlement2);
        settlementRepository.save(settlement3);
        //when
        List<SettlementResponse> list = settlementRepository
            .findAllByAccommodationAndCouponUseDateAfterAndCouponUseDateBeforeOrderByCouponUseDateDesc
                (PageRequest.of(0, 10), accommodation,
                    LocalDate.of(2023, 9, 27),
                    LocalDate.of(2024, 3, 27))
            .stream().map(SettlementResponse::from).toList();
        //then
        assertThat(list.get(0).couponUseDate()).isEqualTo(settlement2.getCouponUseDate());
        assertThat(list.get(1).couponUseDate()).isEqualTo(settlement1.getCouponUseDate());
    }
}
