package com.coolpeace.domain.statistics.service;

import com.coolpeace.domain.accommodation.entity.Accommodation;
import com.coolpeace.domain.accommodation.repository.AccommodationRepository;
import com.coolpeace.domain.coupon.entity.Coupon;
import com.coolpeace.domain.coupon.exception.CouponNotFoundException;
import com.coolpeace.domain.coupon.repository.CouponRepository;
import com.coolpeace.domain.member.entity.Member;
import com.coolpeace.domain.settlement.entity.Settlement;
import com.coolpeace.domain.settlement.repository.SettlementRepository;
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
    private final SettlementRepository settlementRepository;

    public void updateMonthlySum() {
        int month = LocalDate.now().getMonthValue();
        int year = LocalDate.now().getYear();
        List<Accommodation> accommodations = accommodationRepository.findAll();

        accommodations.forEach(accommodation -> {
            Member member = accommodation.getMember();
            List<DailyStatistics> dailyStatisticsList = dailyStatisticsRepository
                .findAllByAccommodation(accommodation);

            if (!dailyStatisticsList.isEmpty()) {
                MonthlyStatistics monthlyStatistics = monthlyStatisticsRepository
                    .save(new MonthlyStatistics(year, month, member, accommodation));

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

    public void completeSettlement() {
        List<Accommodation> accommodations = accommodationRepository.findAll();

        accommodations.forEach(accommodation -> {
            List<Settlement> settlements = settlementRepository.
                findAllByAccommodationForDailyUpdate(accommodation);
            for (Settlement settlement : settlements) {
                settlement.completeSettlement();
            }
        });
    }

    public void updateCouponDownloadTop3() {
        int month = LocalDate.now().getMonthValue();
        int year = LocalDate.now().getYear();
        List<LocalCouponDownload> localCouponDownloads = localCouponDownloadRepository.findAll();
        localCouponDownloads.forEach(localCouponDownload -> {
            List<Accommodation> accommodations = accommodationRepository.findAllBySigunguName(
                localCouponDownload.getRegion());
            accommodations.forEach(accommodation -> {
                Member member = accommodation.getMember();
                monthlyStatisticsRepository
                    .findByAccommodationAndStatisticsYearAndStatisticsMonth(accommodation, year,
                        month)
                    .orElse(monthlyStatisticsRepository.save
                        (new MonthlyStatistics(year, month, member, accommodation)))
                    .setLocalCouponDownloadTop3(localCouponDownload);
            });
        });
    }

    public void updateLocalCouponDownload() {
        localCouponDownloadRepository.deleteAllInBatch();
        List<Coupon> coupons = couponRepository.findAll();
        for (Coupon coupon : coupons) {
            String address = coupon.getAccommodation().getSigungu().getName();
            LocalCouponDownload localCouponDownload = localCouponDownloadRepository
                .findByRegion(address)
                .orElseGet(
                    () -> localCouponDownloadRepository.save(new LocalCouponDownload(address)));
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

    private int getDownloadCountFromId(Long couponId) {
        return couponRepository.findById(couponId)
            .orElseThrow(CouponNotFoundException::new).getDownloadCount();
    }
}
