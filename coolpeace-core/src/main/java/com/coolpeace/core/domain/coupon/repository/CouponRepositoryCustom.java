package com.coolpeace.core.domain.coupon.repository;


import com.coolpeace.core.domain.coupon.entity.Coupon;
import com.coolpeace.core.domain.coupon.dto.request.SearchCouponParams;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

public interface CouponRepositoryCustom {

    Page<Coupon> findAllCoupons(Long memberId, SearchCouponParams params, PageRequest pageable);

    Optional<Coupon> findRecentCouponByMemberId(Long memberId);

    Optional<Coupon> findByCouponNumber(String couponNumber);

    List<Coupon> exposureCoupons(Long memberId, Long accommodationId);

    Boolean noRegister(Long memberId,Long accommodationId);

    Boolean noExposure(Long memberId,Long accommodationId);

    List<Coupon> expiration3days(Long memberId,Long accommodationId);


}
