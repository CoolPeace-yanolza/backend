package com.coolpeace.domain.statistics.service;

import com.coolpeace.domain.accommodation.entity.Accommodation;
import com.coolpeace.domain.accommodation.repository.AccommodationRepository;
import com.coolpeace.domain.coupon.entity.Coupon;
import com.coolpeace.domain.coupon.repository.CouponRepository;
import com.coolpeace.domain.member.entity.Member;
import com.coolpeace.domain.member.repository.MemberRepository;
import com.coolpeace.domain.settlement.entity.Settlement;
import com.coolpeace.domain.settlement.repository.SettlementRepository;
import com.coolpeace.domain.statistics.entity.DailyStatistics;
import com.coolpeace.domain.statistics.entity.LocalCouponDownload;
import com.coolpeace.domain.statistics.entity.MonthlyStatistics;
import com.coolpeace.domain.statistics.exception.MonthlyStatisticsNotFoundException;
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
    private final MemberRepository memberRepository;
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
                MonthlyStatistics monthlyStatistics = monthlyStatisticsRepository.save(
                    new MonthlyStatistics(year, month, member,
                        dailyStatisticsList.get(0).getAccommodation()));

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

    public void completeSettlement(){
        List<Accommodation> accommodations = accommodationRepository.findAll();

        accommodations.forEach(accommodation -> {
            List<Settlement> settlements = settlementRepository
                .findAllByAccommodationForDailyUpdate(accommodation);

            for (Settlement settlement : settlements) {
                settlement.completeSettlement();
            }
        });
    }

    /* 숙소 지역 로직 구현되면, 변경 예정 */
    public void updateCouponDownloadTop3() {
        int month = LocalDate.now().getMonthValue();
        int year = LocalDate.now().getYear();
        List<LocalCouponDownload> localCouponDownloads = localCouponDownloadRepository.findAll();
        localCouponDownloads.forEach(localCouponDownload -> {
            List<Accommodation> accommodations = accommodationRepository.findByAddress(
                localCouponDownload.getRegion());
            accommodations.forEach(
                accommodation -> {
                    monthlyStatisticsRepository
                    .findByAccommodationAndStatisticsYearAndStatisticsMonth(accommodation, year, month)
                    .orElseThrow(MonthlyStatisticsNotFoundException::new)
                    .setLocalCouponDownloadTop3(localCouponDownload);});

        });
    }

    /* 숙소 지역 로직 구현되면, 변경 예정 */
    public void updateLocalCouponDownload() {
        localCouponDownloadRepository.deleteAllInBatch();
        List<Coupon> coupons = couponRepository.findAll();
        coupons.forEach(coupon -> {
            String address = coupon.getAccommodation().getAddress();
            LocalCouponDownload localCouponDownload = localCouponDownloadRepository.findByRegion(
                address).orElseGet(
                () -> localCouponDownloadRepository.save(new LocalCouponDownload(address)));

            if (setLocalCouponTitle(coupon, localCouponDownload)) {
                return;
            }
            updateLocalCouponTitle(coupon, localCouponDownload);
        });
    }


    private boolean setLocalCouponTitle(Coupon coupon, LocalCouponDownload localCouponDownload) {
        boolean isReturn = false;
      /*  int downloadCount = coupon.getDownLoadCount();
        if (localCouponDownload.getFirstCouponTitle().isBlank()) {
            localCouponDownload.setFirstCouponTitle(coupon.getTitle());
            isReturn = true;
        }
        if (localCouponDownload.getSecondCouponTitle().isBlank()) {
            localCouponDownload.setSecondCouponTitle(coupon.getTitle());
            isReturn = true;
        }
        if (localCouponDownload.getThirdCouponTitle().isBlank()) {
            localCouponDownload.setThirdCouponTitle(coupon.getTitle());
            isReturn = true;
        }*/
        return isReturn;
    }

    private void updateLocalCouponTitle(Coupon coupon, LocalCouponDownload localCouponDownload) {
        /*int downloadCount = coupon.getDownLoadCount();
        int downloadCount1st = getDownloadCountFromTitle(localCouponDownload.getFirstCouponTitle());
        int downloadCount2nd = getDownloadCountFromTitle(localCouponDownload.getSecondCouponTitle());
        int downloadCount3rd = getDownloadCountFromTitle(localCouponDownload.getThirdCouponTitle());

        if (downloadCount > downloadCount1st) {
            String temp1st = localCouponDownload.getFirstCouponTitle();
            String temp2nd = localCouponDownload.getSecondCouponTitle();
            localCouponDownload.setFirstCouponTitle(coupon.getTitle());
            localCouponDownload.setSecondCouponTitle(temp1st);
            localCouponDownload.setThirdCouponTitle(temp2nd);
        } else if (downloadCount > downloadCount2nd) {
            String temp2nd = localCouponDownload.getSecondCouponTitle();
            localCouponDownload.setSecondCouponTitle(coupon.getTitle());
            localCouponDownload.setThirdCouponTitle(temp2nd);
        } else if (downloadCount > downloadCount3rd) {
            localCouponDownload.setThirdCouponTitle(coupon.getTitle());
        }*/
    }

   /* private int getDownloadCountFromTitle(String title) {
        return couponRepository.findByTitle(title)
            .orElseThrow(CouponException::new)
            .getDownLoadCount();
    }*/
}
