package com.coolpeace.domain.coupon.repository;

import com.coolpeace.domain.accommodation.entity.Accommodation;
import com.coolpeace.domain.coupon.entity.Coupon;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon,Long>,CouponRepositoryCustom {
    boolean existsByMemberIdAndCouponNumber(Long memberId, String couponNumber);
    List<Coupon> findAllByExposureStartDateGreaterThanEqual(LocalDate localDate);
    List<Coupon> findAllByAccommodationAndExposureStartDateGreaterThanEqual(Accommodation accommodation,LocalDate localDate);
}
