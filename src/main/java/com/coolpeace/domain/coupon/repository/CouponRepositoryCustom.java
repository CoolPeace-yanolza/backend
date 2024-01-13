package com.coolpeace.domain.coupon.repository;


import com.coolpeace.domain.coupon.dto.request.SearchCouponParams;
import com.coolpeace.domain.coupon.entity.Coupon;
import com.coolpeace.domain.coupon.entity.type.CouponStatusType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CouponRepositoryCustom {

    Page<Coupon> findAllCoupons(Long memberId, SearchCouponParams params, PageRequest pageable);

    Map<CouponStatusType, Long> countCouponsByCouponStatus();

    List<Coupon> findRecentCouponByMemberId(Long memberId);

    Optional<Coupon> findByCouponNumber(String couponNumber);

    List<Coupon> exposureCoupons(Long memberId, Long accommodationId);

    Boolean noRegister(Long memberId,Long accommodationId);

    Boolean noExposure(Long memberId,Long accommodationId);

    List<Coupon> expiration3days(Long memberId,Long accommodationId);


}
