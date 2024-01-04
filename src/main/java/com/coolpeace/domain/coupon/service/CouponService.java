package com.coolpeace.domain.coupon.service;

import com.coolpeace.domain.coupon.dto.request.CouponExposeRequest;
import com.coolpeace.domain.coupon.dto.request.CouponRegisterRequest;
import com.coolpeace.domain.coupon.dto.request.CouponUpdateRequest;
import com.coolpeace.domain.coupon.dto.response.CouponRegisterResponse;
import com.coolpeace.domain.coupon.dto.response.CouponResponse;
import com.coolpeace.domain.coupon.entity.Coupon;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
public class CouponService {

    @Transactional(readOnly = true)
    public Coupon getRecentHistory() {
        return null;
    }

    public CouponRegisterResponse register(CouponRegisterRequest couponRegisterRequest) {
        return null;
    }

    public List<CouponResponse> searchCoupons() {
        return Collections.emptyList();
    }

    public void updateCoupon(String couponId, CouponUpdateRequest couponExposeRequest) {
    }

    public void exposeCoupon(String couponId, CouponExposeRequest couponExposeRequest) {
    }

    public void deleteCoupon(String couponId) {
    }
}
