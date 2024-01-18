package com.coolpeace.domain.statistics.service;

import com.coolpeace.domain.accommodation.entity.Accommodation;
import com.coolpeace.domain.accommodation.repository.AccommodationRepository;
import com.coolpeace.domain.coupon.entity.Coupon;
import com.coolpeace.domain.coupon.exception.CouponNotFoundException;
import com.coolpeace.domain.coupon.repository.CouponRepository;
import com.coolpeace.domain.member.entity.Member;
import com.coolpeace.domain.statistics.entity.DailyStatistics;
import com.coolpeace.domain.statistics.entity.LocalCouponDownload;
import com.coolpeace.domain.statistics.entity.MonthlyStatistics;
import com.coolpeace.domain.statistics.repository.DailyStatisticsRepository;
import com.coolpeace.domain.statistics.repository.LocalCouponDownloadRepository;
import com.coolpeace.domain.statistics.repository.MonthlyStatisticsRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MonthlyStatisticsService {

    private final DailyStatisticsRepository dailyStatisticsRepository;
    private final MonthlyStatisticsRepository monthlyStatisticsRepository;
    private final CouponRepository couponRepository;
    private final LocalCouponDownloadRepository localCouponDownloadRepository;
    private final AccommodationRepository accommodationRepository;

    public void updateMonthlySum(int statisticsYear, int statisticsMonth) {
        int month = checkMonth(statisticsMonth);
        int year = checkYear(statisticsYear);
        List<Accommodation> accommodations = accommodationRepository.findAll();

        accommodations.forEach(accommodation -> {
            Member member = accommodation.getMember();
            List<DailyStatistics> dailyStatisticsList = dailyStatisticsRepository
                .findAllByAccommodation(accommodation);

            if (!dailyStatisticsList.isEmpty()) {
                MonthlyStatistics monthlyStatistics = monthlyStatisticsRepository
                    .findByAccommodationAndStatisticsYearAndStatisticsMonth(accommodation, year,
                        month)
                    .orElseGet(()->monthlyStatisticsRepository.save
                        (new MonthlyStatistics(year, month, member, accommodation)));

                dailyStatisticsList.forEach(dailyStatistics -> monthlyStatistics.setMonthlySum(
                    dailyStatistics.getTotalSales(),
                    dailyStatistics.getCouponTotalSales(),
                    dailyStatistics.getDownloadCount(),
                    dailyStatistics.getUsedCount(),
                    dailyStatistics.getSettlementAmount()
                ));

                dailyStatisticsRepository.deleteAllInBatch(dailyStatisticsList);
            }
        });
    }


    public void updateCouponDownloadTop3(int statisticsYear, int statisticsMonth) {
        int month = checkMonth(statisticsMonth);
        int year = checkYear(statisticsYear);
        List<LocalCouponDownload> localCouponDownloads = localCouponDownloadRepository
            .findAllByStatisticsYearAndStatisticsMonth(year,month);
        localCouponDownloads.forEach(localCouponDownload -> {
            List<Accommodation> accommodations = accommodationRepository.findAllBySigunguName(
                localCouponDownload.getRegion());
            accommodations.forEach(accommodation -> {
                Member member = accommodation.getMember();
                monthlyStatisticsRepository
                    .findByAccommodationAndStatisticsYearAndStatisticsMonth(accommodation, year,
                        month)
                    .orElseGet(()->monthlyStatisticsRepository.save
                        (new MonthlyStatistics(year, month, member, accommodation)))
                    .setLocalCouponDownloadTop3(localCouponDownload);
            });
        });
    }

    public void updateLocalCouponDownload(int statisticsYear, int statisticsMonth) {
        int month = checkMonth(statisticsMonth);
        int year = checkYear(statisticsYear);
        List<Coupon> coupons = couponRepository.findAllByExposureStartDateGreaterThanEqual
            (LocalDate.of(year,month,1));
        for (Coupon coupon : coupons) {
            String address = coupon.getAccommodation().getSigungu().getName();
            LocalCouponDownload localCouponDownload = localCouponDownloadRepository
                .findByRegionAndStatisticsYearAndStatisticsMonth(address,year,month).orElseGet
                    (()->localCouponDownloadRepository
                        .save(new LocalCouponDownload(address,year,month)));
            if (localCouponDownload.getFirstCouponTitle().isBlank()) {
                localCouponDownload.init(coupon.getCouponTitle(), coupon.getId());
                continue;
            }
            updateLocalCouponTitle(coupon, localCouponDownload);
        }
    }

    private void updateLocalCouponTitle(Coupon coupon, LocalCouponDownload localCouponDownload) {
        int downloadCount = coupon.getDownloadCount();
        int downloadCount1st = getDownloadCountFromId(localCouponDownload.getFirstCouponId());
        int downloadCount2nd = getDownloadCountFromId(localCouponDownload.getSecondCouponId());
        int downloadCount3rd = getDownloadCountFromId(localCouponDownload.getThirdCouponId());
        String nowCouponTitle = coupon.getCouponTitle();
        Long nowCouponId = coupon.getId();

        if (downloadCount > downloadCount1st) {
            localCouponDownload.setAllCoupon(nowCouponTitle, nowCouponId, localCouponDownload);
        } else if (downloadCount > downloadCount2nd) {
            localCouponDownload.setSecondAndThirdCoupon(nowCouponTitle, nowCouponId,
                localCouponDownload);
        } else if (downloadCount > downloadCount3rd) {
            localCouponDownload.setThirdCoupon(nowCouponTitle, nowCouponId);
        }

    }

    public void updateLocalCouponAvg(int statisticsYear, int statisticsMonth) {
        int month = checkMonth(statisticsMonth);
        int year = checkYear(statisticsYear);
        localCouponDownloadRepository.findAllByStatisticsYearAndStatisticsMonth(year,month)
            .forEach(localCouponDownload -> {
            List<Accommodation> accommodations = accommodationRepository.findAllBySigunguName(
                localCouponDownload.getRegion());
            for (Accommodation accommodation : accommodations) {
                localCouponDownload.setCount(couponRepository
                        .findAllByAccommodationAndExposureStartDateGreaterThanEqual
                            (accommodation,LocalDate.of(year,month,1)).size(),
                    accommodation.getAccommodationType());
            }
        });
    }

    private int getDownloadCountFromId(Long couponId) {
        return couponRepository.findById(couponId)
            .orElseThrow(CouponNotFoundException::new).getDownloadCount();
    }

    private int checkYear(int year) {
        if (year == 0 ) {
            return LocalDate.now().getYear();
        }
        return year;
    }
    private int checkMonth(int month) {
        if (month == 0 ) {
            return LocalDate.now().getDayOfMonth();
        }
        return month;
    }

}
