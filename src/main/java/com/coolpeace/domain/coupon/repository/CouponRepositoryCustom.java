package com.coolpeace.domain.coupon.repository;


import com.coolpeace.domain.coupon.dto.request.SearchCouponParams;
import com.coolpeace.domain.coupon.entity.Coupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface CouponRepositoryCustom {

    Page<Coupon> findAllCoupons(Long memberId, SearchCouponParams params, PageRequest pageable);

    List<Coupon> exposureCoupons(Long memberId, Long accommodationId);

    Boolean noRegister(Long memberId,Long accommodationId);

    Boolean noExposure(Long memberId,Long accommodationId);

    List<Coupon> expiration3days(Long memberId,Long accommodationId);


}
