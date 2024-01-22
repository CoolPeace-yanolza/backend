package com.coolpeace.domain.settlement.service;

import com.coolpeace.domain.accommodation.entity.Accommodation;
import com.coolpeace.domain.accommodation.exception.AccommodationNotFoundException;
import com.coolpeace.domain.accommodation.exception.AccommodationNotMatchMemberException;
import com.coolpeace.domain.accommodation.repository.AccommodationRepository;
import com.coolpeace.domain.member.entity.Member;
import com.coolpeace.domain.member.exception.MemberNotFoundException;
import com.coolpeace.domain.member.repository.MemberRepository;
import com.coolpeace.domain.settlement.dto.request.SearchSettlementParams;
import com.coolpeace.domain.settlement.dto.response.PageSettlementResponse;
import com.coolpeace.domain.settlement.dto.response.SettlementResponse;
import com.coolpeace.domain.settlement.dto.response.SumSettlementResponse;
import com.coolpeace.domain.statistics.entity.MonthlySearchDate;
import com.coolpeace.domain.settlement.entity.Settlement;
import com.coolpeace.domain.settlement.repository.SettlementRepository;
import com.coolpeace.domain.statistics.entity.DailyStatistics;
import com.coolpeace.domain.statistics.entity.MonthlyStatistics;
import com.coolpeace.domain.statistics.repository.DailyStatisticsRepository;
import com.coolpeace.domain.statistics.repository.MonthlyStatisticsRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        MonthlySearchDate monthlySearchDate = MonthlySearchDate.getMonthlySearchDate(0,0);
        List<DailyStatistics> dailyStatisticsList = dailyStatisticsRepository
            .findAllByAccommodation(accommodation);

        int thisMonthSumSettlement = 0;
        for (DailyStatistics dailyStatistics : dailyStatisticsList) {
            thisMonthSumSettlement += dailyStatistics.getSettlementAmount();
        }
        int lastMonthSumSettlement = 0 ;

        lastMonthSumSettlement = monthlyStatisticsRepository
            .findByAccommodationAndStatisticsYearAndStatisticsMonth
                (accommodation, monthlySearchDate.year(), monthlySearchDate.month())
            .orElseGet(MonthlyStatistics::emptyMonthlyStatistics).getSettlementAmount();

        return SumSettlementResponse.from(thisMonthSumSettlement, lastMonthSumSettlement);
    }

    public PageSettlementResponse searchSettlement(String memberId, Long accommodationId,
        SearchSettlementParams searchSettlementParams, int page, int pageSize) {

        Accommodation accommodation = checkAccommodationMatchMember(memberId, accommodationId);
        LocalDate startDate = LocalDate.parse(searchSettlementParams.start());
        LocalDate endDate = LocalDate.parse(searchSettlementParams.end());

        Page<Settlement> settlements = switch (searchSettlementParams.order()) {
            case COMPLETE_AT -> settlementRepository
                .findAllByAccommodationAndCouponUseDateGreaterThanEqualAndCouponUseDateLessThanEqualOrderByCompleteAtDesc
                    (PageRequest.of(page,pageSize), accommodation, startDate, endDate);
            case SUM_PRICE -> settlementRepository
                .findAllByAccommodationAndCouponUseDateGreaterThanEqualAndCouponUseDateLessThanEqualOrderBySumPriceDesc
                    (PageRequest.of(page,pageSize), accommodation, startDate, endDate);
            case COUPON_USE_DATE -> settlementRepository
                .findAllByAccommodationAndCouponUseDateGreaterThanEqualAndCouponUseDateLessThanEqualOrderByCouponUseDateDesc
                    (PageRequest.of(page,pageSize), accommodation, startDate, endDate);
            case COUPON_COUNT -> settlementRepository
                .findAllByAccommodationAndCouponUseDateGreaterThanEqualAndCouponUseDateLessThanEqualOrderByCouponCountDesc
                    (PageRequest.of(page,pageSize), accommodation, startDate, endDate);
        };

        long totalElements = settlements.getTotalElements();
        int totalPages = settlements.getTotalPages();

        List<SettlementResponse> settlementResponses =
            settlements.stream().map(SettlementResponse::from).toList();

        return PageSettlementResponse.from(totalElements, totalPages, settlementResponses);

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
