package com.coolpeace.domain.statistics.repository;

import com.coolpeace.domain.statistics.entity.LocalCouponDownload;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocalCouponDownloadRepository extends JpaRepository<LocalCouponDownload,Long> {

    Optional<LocalCouponDownload> findByRegionAndStatisticsYearAndStatisticsMonth(String region,int year, int month);
    List<LocalCouponDownload> findAllByStatisticsYearAndStatisticsMonth(int year, int month);
}
