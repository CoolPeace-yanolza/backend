package com.coolpeace.api.domain.statistics.service;

import com.coolpeace.api.domain.accommodation.entity.Accommodation;
import com.coolpeace.api.domain.accommodation.repository.AccommodationRepository;
import com.coolpeace.api.domain.member.entity.Member;
import com.coolpeace.api.domain.reservation.entity.Reservation;
import com.coolpeace.api.domain.settlement.entity.Settlement;
import com.coolpeace.api.domain.statistics.repository.DailyStatisticsRepository;
import com.coolpeace.api.domain.coupon.repository.CouponRepository;
import com.coolpeace.api.domain.reservation.repository.ReservationRepository;
import com.coolpeace.api.domain.settlement.repository.SettlementRepository;
import com.coolpeace.api.domain.statistics.entity.DailyStatistics;
import com.coolpeace.api.domain.statistics.exception.DailyStatisticsNotFoundException;
import org.springframework.transaction.annotation.Transactional;
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
        int day = LocalDate.now().getDayOfMonth();

        accommodations.forEach(accommodation -> {
            List<Settlement> settlements = settlementRepository
                .findAllByAccommodationForDailyUpdate(accommodation);

            int sumSettlement = 0;

            for (Settlement settlement : settlements) {
                settlement.sumPrice();
                sumSettlement += settlement.getSumPrice();
            }

            DailyStatistics dailyStatistics = dailyStatisticsRepository
                .findByAccommodationAndStatisticsDay(accommodation,day)
                .orElseThrow(DailyStatisticsNotFoundException::new);
            dailyStatistics.setSettlement(sumSettlement);
        });

        }
    }



