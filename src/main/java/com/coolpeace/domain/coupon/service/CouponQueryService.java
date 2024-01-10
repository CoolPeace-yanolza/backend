package com.coolpeace.domain.coupon.service;

import com.coolpeace.domain.coupon.dto.response.CouponDailyResponse;
import com.coolpeace.domain.coupon.entity.Coupon;
import com.coolpeace.domain.coupon.entity.CouponDailyCondition;
import com.coolpeace.domain.coupon.repository.CouponRepository;
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
    public CouponDailyResponse dailyReport(String jwtPrincipal, Long accommodationId) {
        Long memberId = Long.valueOf(jwtPrincipal);
        if (Boolean.TRUE.equals(couponRepository.noRegister(memberId, accommodationId))) {
            return CouponDailyResponse.from(CouponDailyCondition.NO_REGISTER);
        }
        List<Coupon> expiration3daysCoupons = couponRepository.expiration3days(memberId,
            accommodationId);
        if (!expiration3daysCoupons.isEmpty()) {
            return CouponDailyResponse.from(CouponDailyCondition.EXPIRATION_3DAYS,
                expiration3daysCoupons.stream().map(Coupon::getCouponTitle).toList());
        }
        if (Boolean.TRUE.equals(couponRepository.noExposure(memberId, accommodationId))) {
            return CouponDailyResponse.from(CouponDailyCondition.NO_EXPOSURE);
        }

        List<Coupon> exposureCoupons = couponRepository.exposureCoupons(memberId, accommodationId);
        return CouponDailyResponse.from(CouponDailyCondition.NO_CONDITION,
            exposureCoupons.stream().map(Coupon::getCouponTitle).toList());
    }
}
