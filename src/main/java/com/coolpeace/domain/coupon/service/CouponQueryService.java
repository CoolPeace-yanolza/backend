package com.coolpeace.domain.coupon.service;

import com.coolpeace.domain.coupon.dto.request.CouponDailyRequest;
import com.coolpeace.domain.coupon.dto.response.CouponDailyResponse;
import com.coolpeace.domain.coupon.entity.Coupon;
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
    public CouponDailyResponse dailyReport(CouponDailyRequest request) {
        if (couponRepository.NoRegister(request.memberId(), request.accommodationId())) {
            return CouponDailyResponse.builder().condition("등록된 쿠폰이 없음").build();
        }
        if (couponRepository.NoExposure(request.memberId(), request.accommodationId())) {
            return CouponDailyResponse.builder().condition("노출 중인 쿠폰이 없음").build();
        }

        List<Coupon> expiration3daysCoupons = couponRepository.expiration3days(request.memberId(),
            request.accommodationId());
        if (!expiration3daysCoupons.isEmpty()) {
            return CouponDailyResponse.builder().condition("곧 만료되는 쿠폰이 있음")
                .couponTitle(expiration3daysCoupons.stream().map(Coupon::getTitle).toList())
                .build();
        }
        List<Coupon> exposureCoupons = couponRepository.exposureCoupons(request.memberId(),
            request.accommodationId());
        return CouponDailyResponse.builder().condition("아무 조건에 해당하지 않음")
            .couponTitle(exposureCoupons.stream().map(Coupon::getTitle).toList())
            .build();
    }
}
