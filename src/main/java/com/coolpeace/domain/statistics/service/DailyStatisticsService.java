package com.coolpeace.domain.statistics.service;

import com.coolpeace.domain.accommodation.entity.Accommodation;
import com.coolpeace.domain.accommodation.repository.AccommodationRepository;
import com.coolpeace.domain.coupon.entity.Coupon;
import com.coolpeace.domain.coupon.entity.type.CouponStatusType;
import com.coolpeace.domain.coupon.repository.CouponRepository;
import com.coolpeace.domain.member.entity.Member;
import com.coolpeace.domain.reservation.entity.Reservation;
import com.coolpeace.domain.reservation.repository.ReservationRepository;
import com.coolpeace.domain.settlement.entity.Settlement;
import com.coolpeace.domain.settlement.repository.SettlementRepository;
import com.coolpeace.domain.statistics.entity.DailySearchDate;
import com.coolpeace.domain.statistics.entity.DailyStatistics;
import com.coolpeace.domain.statistics.exception.DailyStatisticsNotFoundException;
import com.coolpeace.domain.statistics.repository.DailyStatisticsRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DailyStatisticsService {
    private final AccommodationRepository accommodationRepository;

    private final DailyStatisticsRepository dailyStatisticsRepository;

    private final ReservationRepository reservationRepository;

    private final CouponRepository couponRepository;

    private final SettlementRepository settlementRepository;

    @CacheEvict(value = "weeklyCoupon", cacheManager = "contentCacheManager")
    public void updateSales(int statisticsYear, int statisticsMonth, int statisticsDay){
        DailySearchDate dailySearchDate = DailySearchDate.
            getDailySearchDate(statisticsYear,statisticsMonth,statisticsDay);
        int year = dailySearchDate.year();
        int month = dailySearchDate.month();
        int day = dailySearchDate.day();

        List<Accommodation> accommodations = accommodationRepository.findAll();
        accommodations.forEach(accommodation -> {
            Member member = accommodation.getMember();

            List<Reservation> reservations = reservationRepository
                .findByAccommodation(year,month,day,accommodation);

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

    public void updateCoupon(int statisticsYear, int statisticsMonth, int statisticsDay){
        DailySearchDate dailySearchDate = DailySearchDate.
            getDailySearchDate(statisticsYear,statisticsMonth,statisticsDay);
        int year = dailySearchDate.year();
        int month = dailySearchDate.month();
        int day = dailySearchDate.day();

        List<Accommodation> accommodations = accommodationRepository.findAll();
        accommodations.forEach(accommodation -> {
            int downloadCount = 0;
            int usedCount = 0;
            Member member = accommodation.getMember();
            List<Coupon> coupons = couponRepository.
                findAllByExposureDate(accommodation,LocalDate.of(year,month,day));

            for (Coupon coupon : coupons) {
                downloadCount += coupon.getDownloadCount();
                usedCount += coupon.getUseCount();
            }
            DailyStatistics dailyStatistics = dailyStatisticsRepository
                .findByAccommodationAndStatisticsDay(accommodation,day)
                .orElseGet(()->new DailyStatistics(day,member, accommodation));
            dailyStatistics.setCoupon(downloadCount, usedCount);
        });
    }

    @CacheEvict(value = "sumSettlement", cacheManager = "contentCacheManager")
    public void updateSettlement(int statisticsYear, int statisticsMonth, int statisticsDay){
        DailySearchDate dailySearchDate = DailySearchDate.
            getDailySearchDate(statisticsYear,statisticsMonth,statisticsDay);
        int year = dailySearchDate.year();
        int month = dailySearchDate.month();
        int day = dailySearchDate.day();

        List<Accommodation> accommodations = accommodationRepository.findAll();

        accommodations.forEach(accommodation -> {
            List<Settlement> settlements = settlementRepository
                .findAllByAccommodationForDailyUpdate(LocalDate.of(year,month,day),accommodation);

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

    public void updateCouponStatusStartExposure() {
        LocalDate now = LocalDate.now();
        List<Coupon> coupons = couponRepository.startExposureCoupons(now);
        coupons.forEach(coupon -> coupon.changeCouponStatus(CouponStatusType.EXPOSURE_ON));
    }
    public void updateCouponStatusEndExposure() {
        LocalDate now = LocalDate.now();
        List<Coupon> coupons = couponRepository.endExposureCoupons(now);
        coupons.forEach(coupon -> coupon.changeCouponStatus(CouponStatusType.EXPOSURE_END));

    }

}



