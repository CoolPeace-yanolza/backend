package com.coolpeace.domain.settlement.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import com.coolpeace.domain.accommodation.entity.Accommodation;
import com.coolpeace.domain.accommodation.exception.AccommodationNotFoundException;
import com.coolpeace.domain.accommodation.exception.AccommodationNotMatchMemberException;
import com.coolpeace.domain.accommodation.repository.AccommodationRepository;
import com.coolpeace.domain.member.entity.Member;
import com.coolpeace.domain.member.exception.MemberNotFoundException;
import com.coolpeace.domain.member.repository.MemberRepository;
import com.coolpeace.domain.settlement.dto.request.SearchSettlementParams;
import com.coolpeace.domain.settlement.dto.response.SettlementResponse;
import com.coolpeace.domain.settlement.dto.response.SumSettlementResponse;
import com.coolpeace.domain.settlement.entity.Settlement;
import com.coolpeace.domain.settlement.repository.OrderBy;
import com.coolpeace.domain.settlement.repository.SettlementRepository;
import com.coolpeace.domain.statistics.entity.DailyStatistics;
import com.coolpeace.domain.statistics.entity.MonthlyStatistics;
import com.coolpeace.domain.statistics.repository.DailyStatisticsRepository;
import com.coolpeace.domain.statistics.repository.MonthlyStatisticsRepository;
import com.coolpeace.global.builder.MemberTestBuilder;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@ExtendWith(MockitoExtension.class)
class SettlementServiceTest {

    @InjectMocks
    private SettlementService settlementService;
    @Mock
    private SettlementRepository settlementRepository;
    @Mock
    private DailyStatisticsRepository dailyStatisticsRepository;
    @Mock
    private MonthlyStatisticsRepository monthlyStatisticsRepository;
    @Mock
    private AccommodationRepository accommodationRepository;
    @Mock
    private MemberRepository memberRepository;

    @Nested
    @DisplayName("checkAccommodationMatchMember()는")
    class checkAccommodationMatchMember {

        @Test
        @DisplayName("숙박을 반환한다.")
        void _success() {
            //given
            Member member = new MemberTestBuilder().encoded().build();
            Accommodation accommodation = new Accommodation(1L, "신라호텔", "주소주소", member);

            given(accommodationRepository.findById(anyLong())).willReturn(
                Optional.of(accommodation));
            given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));
            //when
            Accommodation getAccommodation =
                settlementService.checkAccommodationMatchMember("1", 1L);
            //then
            assertThat(getAccommodation).extracting("id", "name", "address")
                .containsExactly(accommodation.getId(),
                    accommodation.getName(), accommodation.getAddress());
        }

        @Test
        @DisplayName("숙박 ID에 해당하지 않은 숙박이 존재하지 않아 실패한다.")
        void accommodation_not_found_fail() {
            //given
            Member member = new MemberTestBuilder().encoded().build();
            //when, then
            assertThatThrownBy(() -> settlementService.checkAccommodationMatchMember("1", 1L))
                .isInstanceOf(AccommodationNotFoundException.class);
        }

        @Test
        @DisplayName("멤버 ID에 해당하지 않은 회원이 존재하지 않아 실패한다.")
        void member_not_found_fail() {
            //given
            Member member = new MemberTestBuilder().encoded().build();
            Accommodation accommodation =
                new Accommodation(1L, "신라호텔", "주소주소", member);

            given(accommodationRepository.findById(anyLong())).willReturn(
                Optional.of(accommodation));
            //when, then
            assertThatThrownBy(() -> settlementService.checkAccommodationMatchMember("1", 1L))
                .isInstanceOf(MemberNotFoundException.class);
        }

        @Test
        @DisplayName("숙박에 등록된 회원과 로그인한 회원이 달라 실패한다.")
        void not_match_fail() {
            //given
            Member member1 = new MemberTestBuilder().encoded().build();
            Member member2 = new MemberTestBuilder().encoded().build();
            Accommodation accommodation =
                new Accommodation(1L, "신라호텔", "주소주소", member1);

            given(accommodationRepository.findById(anyLong())).willReturn(
                Optional.of(accommodation));
            given(memberRepository.findById(anyLong())).willReturn(Optional.of(member2));
            //when, then
            assertThatThrownBy(() -> settlementService.checkAccommodationMatchMember("1", 1L))
                .isInstanceOf(AccommodationNotMatchMemberException.class);
        }
    }

    @Nested
    @DisplayName("sumSettlement()는")
    class sumSettlement {

        @Test
        @DisplayName("정산 요약을 내보낼 수 있다.")
        void _success() {
            //given
            Member member = new MemberTestBuilder().encoded().build();
            Accommodation accommodation =
                new Accommodation(1L, "신라호텔", "주소주소", member);
            DailyStatistics dailyStatistics1 = new DailyStatistics(1L, 1, 1000000,
                100000, 11, 10, 100000, member, accommodation);
            DailyStatistics dailyStatistics2 = new DailyStatistics(2L, 2, 1000000,
                100000, 11, 10, 100000, member, accommodation);
            List<DailyStatistics> dailyStatisticsList = new ArrayList<>();
            dailyStatisticsList.add(dailyStatistics1);
            dailyStatisticsList.add(dailyStatistics2);
            MonthlyStatistics monthlyStatistics = new MonthlyStatistics(1L, 2023, 12, 10000000,
                9000000, 1000, 300, 1000000, member, accommodation);

            given(accommodationRepository.findById(anyLong())).willReturn(
                Optional.of(accommodation));
            given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));
            given(dailyStatisticsRepository.findAllByAccommodation
                (any(Accommodation.class))).willReturn(dailyStatisticsList);
            given(monthlyStatisticsRepository.findByAccommodationAndStatisticsYearAndStatisticsMonth
                (any(Accommodation.class), anyInt(), anyInt()))
                .willReturn(Optional.of(monthlyStatistics));

            //when
            SumSettlementResponse getSettlementResponse = settlementService
                .sumSettlement("1", 1L);

            //then
            assertThat(getSettlementResponse).extracting
                    ("thisMonthSettlementAmount", "lastMonthSettlementAmount")
                .containsExactly(200000, 1000000);
        }
    }

    @Nested
    @DisplayName("searchSettlement()는 ")
    class searchSettlement {

        @Test
        @DisplayName("검색결과를 내보낼 수 있다.")
        void _success() {
            //given
            SearchSettlementParams settlementParams = new SearchSettlementParams
                    ("2023-12-01", "2024-03-31", OrderBy.COUPON_USE_DATE);
            Pageable pageable = Pageable.ofSize(10);

            Member member = new MemberTestBuilder().encoded().build();
            Accommodation accommodation =
                new Accommodation(1L, "신라호텔", "주소주소", member);
            Settlement settlement1 = new Settlement(1L, LocalDate.now(), 10,
                1000, 0, 0, 1000, LocalDate.now().plusMonths(1),accommodation);
            Settlement settlement2 = new Settlement(2L, LocalDate.now(), 10,
                1000, 0, 0, 1000, LocalDate.now().plusMonths(1),accommodation);
            List<Settlement> settlements = new ArrayList<>();
            settlements.add(settlement1);
            settlements.add(settlement2);

            PageRequest pageRequest = PageRequest.of(0, 10);
            Page<Settlement> settlementPage =
                new PageImpl<>(settlements.subList(0, 2), pageRequest, settlements.size());

            given(accommodationRepository.findById(anyLong())).willReturn(
                Optional.of(accommodation));
            given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));
            given(settlementRepository
                .findAllByAccommodationAndCouponUseDateAfterAndCouponUseDateBeforeOrderByCouponUseDateDesc
                    (any(Pageable.class), any(Accommodation.class), any(LocalDate.class),
                        any(LocalDate.class))).willReturn(settlementPage);
            //when
            List<SettlementResponse> settlementResponses = settlementService.searchSettlement("1",
                1L, settlementParams, 1,10);
            //then
            assertThat(settlementResponses.get(0)).extracting
                    ("couponUseDate", "couponCount", "discountPrice", "cancelPrice",
                        "supplyPrice", "sumPrice", "completeAt")
                .containsExactly(settlement1.getCouponUseDate(), settlement1.getCouponCount(),
                    settlement1.getDiscountPrice(), settlement1.getCancelPrice(),
                    settlement1.getSupplyPrice(),
                    settlement1.getSumPrice(), settlement1.getCompleteAt());
            assertThat(settlementResponses.get(1)).extracting
                    ("couponUseDate", "couponCount", "discountPrice", "cancelPrice",
                        "supplyPrice", "sumPrice", "completeAt")
                .containsExactly(settlement1.getCouponUseDate(), settlement2.getCouponCount(),
                    settlement2.getDiscountPrice(), settlement2.getCancelPrice(),
                    settlement2.getSupplyPrice(),
                    settlement2.getSumPrice(), settlement2.getCompleteAt());

        }
    }

}
