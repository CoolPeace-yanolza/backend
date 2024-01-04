package com.coolpeace.domain.coupon.repository;


import com.coolpeace.domain.coupon.entity.Coupon;
import java.util.List;

public interface CouponRepositoryCustom {

    List<Coupon> exposureCoupons(Long memberId, Long accommodationId);

    Boolean noRegister(Long memberId,Long accommodationId);

    Boolean noExposure(Long memberId,Long accommodationId);

    List<Coupon> expiration3days(Long memberId,Long accommodationId);


}
