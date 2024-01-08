package com.coolpeace.domain.statistics.service;

import com.coolpeace.domain.accommodation.entity.Accommodation;
import com.coolpeace.domain.accommodation.repository.AccommodationRepository;
import com.coolpeace.domain.coupon.repository.CouponRepository;
import com.coolpeace.domain.member.entity.Member;
import com.coolpeace.domain.reservation.entity.Reservation;
import com.coolpeace.domain.reservation.repository.ReservationRepository;
import com.coolpeace.domain.settlement.repository.SettlementRepository;
import com.coolpeace.domain.statistics.entity.DailyStatistics;
import com.coolpeace.domain.statistics.exception.DailyStatisticsNotFoundException;
import com.coolpeace.domain.statistics.repository.DailyStatisticsRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class DailyStatisticsService {
    private final AccommodationRepository accommodationRepository;

    private final DailyStatisticsRepository dailyStatisticsRepository;

    private final ReservationRepository reservationRepository;

    private final CouponRepository couponRepository;

    private final SettlementRepository settlementRepository;

    public void updateSales(){
        List<Accommodation> accommodations = accommodationRepository.findAll();
        int day = LocalDate.now().getDayOfMonth();
        accommodations.forEach(accommodation -> {
            Member member = accommodation.getMember();

            List<Reservation> reservations = reservationRepository.findByAccommodation(accommodation);

            int totalSales = reservations.stream()
                .mapToInt(Reservation::getTotalPrice)
                .sum();

            int couponTotalSales = reservations.stream()
                .mapToInt(reservation -> reservation.getTotalPrice() - reservation.getDiscountPrice())
                .sum();

            DailyStatistics dailyStatistics = dailyStatisticsRepository
                .findByAccommodationAndStatisticsDay(accommodation, day)
                .orElseGet(() -> dailyStatisticsRepository
                    .save(new DailyStatistics(day, member, accommodation)));

            dailyStatistics.setSales(totalSales, couponTotalSales);});

    }

    public void updateCoupon(){
        List<Accommodation> accommodations = accommodationRepository.findAll();
        int downloadCount = 0;
        int usedCount = 0;
        int day = LocalDate.now().getDayOfMonth();

        for (Accommodation accommodation : accommodations) {
            Member member = accommodation.getMember();
            /* 쿠폰 변경 후, 적용 예정
            * ...
            * */
            DailyStatistics dailyStatistics = dailyStatisticsRepository
                .findByAccommodationAndStatisticsDay(accommodation,day)
                .orElse(new DailyStatistics(day,member, accommodation));
            dailyStatistics.setCoupon(downloadCount, usedCount);
        }

    }

    public void updateSettlement(){
        List<Accommodation> accommodations = accommodationRepository.findAll();
        int settlementAmount = 0;
        int day = LocalDate.now().getDayOfMonth();

        for (Accommodation accommodation : accommodations) {
            Member member = accommodation.getMember();
//            정산쪽 확정되면 로직 구현 예정
//            settlementRepository.findAllByAccommodation(accommodation).stream()
//                .map(settlement -> {settlement.getCo})
            DailyStatistics dailyStatistics = dailyStatisticsRepository
                .findByAccommodationAndStatisticsDay(accommodation,day)
                .orElseThrow(DailyStatisticsNotFoundException::new);
            dailyStatistics.setSettlement(settlementAmount);
        }
    }


}
