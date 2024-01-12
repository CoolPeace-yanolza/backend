package com.coolpeace.api.domain.coupon.service;

import com.coolpeace.api.domain.coupon.dto.response.CouponDailyResponse;
import com.coolpeace.core.domain.coupon.entity.Coupon;
import com.coolpeace.core.domain.coupon.entity.CouponDailyCondition;
import com.coolpeace.core.domain.coupon.repository.CouponRepository;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CouponQueryService {

    private final CouponRepository couponRepository;

    /* 메소드 복잡도를 낮추는 방법 고안 중 */
    public CouponDailyResponse dailyReport(Long memberId, Long accommodationId) {

        if (couponRepository.noRegister(memberId, accommodationId)) {
            return CouponDailyResponse.from(CouponDailyCondition.NO_REGISTER);
        }
        if (couponRepository.noExposure(memberId, accommodationId)) {
            return CouponDailyResponse.from(CouponDailyCondition.NO_EXPOSURE);
        }

        List<Coupon> expiration3daysCoupons = couponRepository.expiration3days(memberId, accommodationId);
        if (!expiration3daysCoupons.isEmpty()) {
            return CouponDailyResponse.from(CouponDailyCondition.EXPIRATION_3DAYS,
                expiration3daysCoupons.stream().map(Coupon::getTitle).toList());
        }

        List<Coupon> exposureCoupons = couponRepository.exposureCoupons(memberId, accommodationId);
        return CouponDailyResponse.from(CouponDailyCondition.NO_CONDITION,
            exposureCoupons.stream().map(Coupon::getTitle).toList());
    }
}
