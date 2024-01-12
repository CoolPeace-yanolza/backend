package com.coolpeace.api.domain.settlement.service;

import com.coolpeace.api.domain.accommodation.exception.AccommodationNotFoundException;
import com.coolpeace.api.domain.accommodation.exception.AccommodationNotMatchMemberException;
import com.coolpeace.api.domain.member.exception.MemberNotFoundException;
import com.coolpeace.api.domain.settlement.dto.request.SearchSettlementParams;
import com.coolpeace.api.domain.settlement.dto.response.SettlementResponse;
import com.coolpeace.api.domain.settlement.dto.response.SumSettlementResponse;
import com.coolpeace.core.domain.accommodation.entity.Accommodation;
import com.coolpeace.core.domain.accommodation.repository.AccommodationRepository;
import com.coolpeace.core.domain.member.entity.Member;
import com.coolpeace.core.domain.member.repository.MemberRepository;
import com.coolpeace.core.domain.settlement.entity.Settlement;
import com.coolpeace.core.domain.settlement.repository.SettlementRepository;
import com.coolpeace.core.domain.statistics.entity.DailyStatistics;
import com.coolpeace.core.domain.statistics.entity.MonthlyStatistics;
import com.coolpeace.core.domain.statistics.repository.DailyStatisticsRepository;
import com.coolpeace.core.domain.statistics.repository.MonthlyStatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    public SumSettlementResponse sumSettlement(String memberId, Long accommodationId) {
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
                .orElse(MonthlyStatistics.emptyMonthlyStatistics()).getSettlementAmount();

        return SumSettlementResponse.from(thisMonthSumSettlement, lastMonthSumSettlement);
    }

    public List<SettlementResponse> searchSettlement(String memberId, Long accommodationId,
                                                     SearchSettlementParams searchSettlementParams, int page, int pageSize) {

        Accommodation accommodation = checkAccommodationMatchMember(memberId, accommodationId);
        LocalDate startDate = LocalDate.parse(searchSettlementParams.startDate());
        LocalDate endDate = LocalDate.parse(searchSettlementParams.endDate());

        Page<Settlement> settlements = switch (searchSettlementParams.orderBy()) {
            case COMPLETE_AT -> settlementRepository
                    .findAllByAccommodationAndCouponUseDateAfterAndCouponUseDateBeforeOrderByCompleteAtDesc
                            (PageRequest.of(page, pageSize), accommodation, startDate, endDate);
            case SUM_PRICE -> settlementRepository
                    .findAllByAccommodationAndCouponUseDateAfterAndCouponUseDateBeforeOrderBySumPriceDesc
                            (PageRequest.of(page, pageSize), accommodation, startDate, endDate);
            case COUPON_USE_DATE -> settlementRepository
                    .findAllByAccommodationAndCouponUseDateAfterAndCouponUseDateBeforeOrderByCouponUseDateDesc
                            (PageRequest.of(page, pageSize), accommodation, startDate, endDate);
            case COUPON_COUNT -> settlementRepository
                    .findAllByAccommodationAndCouponUseDateAfterAndCouponUseDateBeforeOrderByCouponCountDesc
                            (PageRequest.of(page, pageSize), accommodation, startDate, endDate);
        };
        return settlements.stream().map(SettlementResponse::from).toList();
    }


    public Accommodation checkAccommodationMatchMember(String memberId, Long accommodationId) {
        Accommodation accommodation = accommodationRepository.findById(accommodationId)
                .orElseThrow(AccommodationNotFoundException::new);
        Member member = memberRepository.findById(Long.valueOf(memberId))
                .orElseThrow(MemberNotFoundException::new);
        if (!accommodation.getMember().equals(member)) {
            throw new AccommodationNotMatchMemberException();
        }
        return accommodation;
    }

}
