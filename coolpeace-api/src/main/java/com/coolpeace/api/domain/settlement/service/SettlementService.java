package com.coolpeace.api.domain.settlement.service;

import com.coolpeace.core.domain.accommodation.entity.Accommodation;
import com.coolpeace.api.domain.accommodation.exception.AccommodationNotFoundException;
import com.coolpeace.api.domain.accommodation.exception.AccommodationNotMatchMemberException;
import com.coolpeace.core.domain.accommodation.repository.AccommodationRepository;
import com.coolpeace.core.domain.member.entity.Member;
import com.coolpeace.api.domain.member.exception.MemberNotFoundException;
import com.coolpeace.core.domain.member.repository.MemberRepository;
import com.coolpeace.api.domain.settlement.dto.request.SearchSettlementParams;
import com.coolpeace.api.domain.settlement.dto.response.SettlementResponse;
import com.coolpeace.api.domain.settlement.dto.response.SumSettlementResponse;
import com.coolpeace.core.domain.settlement.entity.Settlement;
import com.coolpeace.core.domain.settlement.repository.SettlementRepository;
import com.coolpeace.core.domain.statistics.entity.DailyStatistics;
import com.coolpeace.core.domain.statistics.exception.MonthlyStatisticsNotFoundException;
import com.coolpeace.core.domain.statistics.repository.DailyStatisticsRepository;
import com.coolpeace.core.domain.statistics.repository.MonthlyStatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SettlementService {

    private final SettlementRepository settlementRepository;
    private final DailyStatisticsRepository dailyStatisticsRepository;
    private final MonthlyStatisticsRepository monthlyStatisticsRepository;
    private final AccommodationRepository accommodationRepository;
    private final MemberRepository memberRepository;

    public SumSettlementResponse sumSettlement(Long memberId, Long accommodationId) {
        Accommodation accommodation = checkAccommodationMatchMember(memberId, accommodationId);
        List<DailyStatistics> dailyStatisticsList = dailyStatisticsRepository
            .findAllByAccommodation(accommodation);

        int thisMonthSumSettlement = 0;
        for (DailyStatistics dailyStatistics : dailyStatisticsList) {
            thisMonthSumSettlement += dailyStatistics.getSettlementAmount();
        }

        int lastMonthSumSettlement = monthlyStatisticsRepository
            .findByAccommodationAndStatisticsYearAndStatisticsMonth
                (accommodation, LocalDate.now().getYear(), LocalDate.now().getMonth().getValue())
            .orElseThrow(MonthlyStatisticsNotFoundException::new).getSettlementAmount();

        return SumSettlementResponse.from(thisMonthSumSettlement, lastMonthSumSettlement);
    }

    public List<SettlementResponse> searchSettlement(Long memberId, Long accommodationId,
                                                     SearchSettlementParams searchSettlementParams, Pageable pageable) {

        Accommodation accommodation = checkAccommodationMatchMember(memberId, accommodationId);
        LocalDate startDate = LocalDate.parse(searchSettlementParams.startDate());
        LocalDate endDate = LocalDate.parse(searchSettlementParams.endDate());

        Page<Settlement> settlements = switch (searchSettlementParams.orderBy()) {
            case COMPLETE_AT -> settlementRepository
                .findAllByAccommodationAndCouponUseDateAfterAndCouponUseDateBeforeOrderByCompleteAt
                    (pageable, accommodation, startDate, endDate);
            case SUM_PRICE -> settlementRepository
                .findAllByAccommodationAndCouponUseDateAfterAndCouponUseDateBeforeOrderBySumPrice
                    (pageable, accommodation, startDate, endDate);
            case COUPON_USE_DATE -> settlementRepository
                .findAllByAccommodationAndCouponUseDateAfterAndCouponUseDateBeforeOrderByCouponUseDate
                    (pageable, accommodation, startDate, endDate);
            case COUPON_COUNT -> settlementRepository
                .findAllByAccommodationAndCouponUseDateAfterAndCouponUseDateBeforeOrderByCouponCount
                    (pageable, accommodation, startDate, endDate);
        };
        return settlements.stream().map(SettlementResponse::from).toList();
    }


    public Accommodation checkAccommodationMatchMember(Long memberId, Long accommodationId) {
        Accommodation accommodation = accommodationRepository.findById(accommodationId)
            .orElseThrow(AccommodationNotFoundException::new);
        Member member = memberRepository.findById(memberId)
            .orElseThrow(MemberNotFoundException::new);
        if (!accommodation.getMember().equals(member)) {
            throw new AccommodationNotMatchMemberException();
        }
        return accommodation;
    }

}
