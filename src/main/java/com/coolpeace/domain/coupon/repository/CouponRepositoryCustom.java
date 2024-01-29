package com.coolpeace.domain.coupon.repository;


import com.coolpeace.domain.accommodation.entity.Accommodation;
import com.coolpeace.domain.coupon.dto.request.SearchCouponParams;
import com.coolpeace.domain.coupon.entity.Coupon;
import com.coolpeace.domain.coupon.entity.type.CouponStatusType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CouponRepositoryCustom {

    Page<Coupon> findAllCoupons(Long memberId, Long accommodationId, SearchCouponParams params, PageRequest pageable);

    Map<CouponStatusType, Long> countCouponsByCouponStatus(Long memberId, Long accommodationId, SearchCouponParams params);

    List<Coupon> findRecentCouponByMemberId(Long memberId);

    Optional<Coupon> findByCouponNumber(String couponNumber);

    List<Coupon> exposureCoupons(Long memberId, Long accommodationId);

    Boolean noRegister(Long memberId,Long accommodationId);

    Boolean noExposure(Long memberId,Long accommodationId);

    List<Coupon> expiration3days(Long memberId,Long accommodationId);

    List<Coupon> findAllByExposureDate(Accommodation accommodation, LocalDate localDate);

    List<Coupon> startExposureCoupons(LocalDate localDate);

    List<Coupon> endExposureCoupons(LocalDate localDate);

}
