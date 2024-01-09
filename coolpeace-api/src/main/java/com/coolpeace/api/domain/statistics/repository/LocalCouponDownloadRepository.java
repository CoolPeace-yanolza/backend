package com.coolpeace.api.domain.statistics.repository;

import com.coolpeace.api.domain.statistics.entity.LocalCouponDownload;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocalCouponDownloadRepository extends JpaRepository<LocalCouponDownload,Long> {

    Optional<LocalCouponDownload> findByRegion(String region);
}
