package com.coolpeace.domain.coupon.service;

import com.coolpeace.domain.coupon.dto.response.CouponDailyResponse;
import com.coolpeace.domain.coupon.entity.Coupon;
import com.coolpeace.domain.coupon.entity.CouponDailyCondition;
import com.coolpeace.domain.coupon.repository.CouponRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CouponQueryService {

    private final CouponRepository couponRepository;

    @Cacheable(value = "dailyReport", key = "#accommodationId",cacheManager = "contentCacheManager")
    public CouponDailyResponse dailyReport(String jwtPrincipal, Long accommodationId) {
        Long memberId = Long.valueOf(jwtPrincipal);

        if (Boolean.TRUE.equals(couponRepository.noRegister(memberId, accommodationId))) {
            return CouponDailyResponse.from(3,CouponDailyCondition.NO_REGISTER);
        }

        List<Coupon> expiration3daysCoupons = couponRepository.expiration3days(memberId,
            accommodationId);
        if (!expiration3daysCoupons.isEmpty()) {
            return CouponDailyResponse.from(1, CouponDailyCondition.EXPIRATION_3DAYS,
                expiration3daysCoupons.stream().map(Coupon::getCouponTitle).toList());
        }

        if (Boolean.TRUE.equals(couponRepository.noExposure(memberId, accommodationId))) {
            return CouponDailyResponse.from(2, CouponDailyCondition.NO_EXPOSURE);
        }

        List<Coupon> exposureCoupons = couponRepository.exposureCoupons(memberId, accommodationId);
        return CouponDailyResponse.from(4, CouponDailyCondition.NO_CONDITION,
            exposureCoupons.stream().map(Coupon::getCouponTitle).toList());
    }
}
