package com.coolpeace.api.domain.coupon.controller;

import com.coolpeace.api.domain.coupon.dto.request.CouponExposeRequest;
import com.coolpeace.api.domain.coupon.dto.request.CouponRegisterRequest;
import com.coolpeace.api.domain.coupon.dto.request.CouponUpdateRequest;
import com.coolpeace.api.domain.coupon.dto.response.CouponCategoryResponse;
import com.coolpeace.api.domain.coupon.dto.response.CouponResponse;
import com.coolpeace.api.domain.coupon.dto.response.CouponSearchResponse;
import com.coolpeace.api.domain.coupon.service.CouponService;
import com.coolpeace.api.global.jwt.security.JwtPrincipal;
import com.coolpeace.api.global.resolver.AuthJwtPrincipal;
import com.coolpeace.core.domain.coupon.dto.request.SearchCouponParams;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/v1/coupons")
@RequiredArgsConstructor
public class CouponController {
    private final CouponService couponService;

    @GetMapping
    public ResponseEntity<CouponSearchResponse> searchCoupons(
            @Valid SearchCouponParams searchCouponParams,
            @PageableDefault Pageable pageable,
            @AuthJwtPrincipal JwtPrincipal jwtPrincipal
    ) {
        Page<CouponResponse> couponResponses = couponService.searchCoupons(
                Long.valueOf(jwtPrincipal.getMemberId()), searchCouponParams, pageable);
        CouponCategoryResponse categoryResponse = couponService.getCouponCategories();
        return ResponseEntity.ok(CouponSearchResponse.from(couponResponses, categoryResponse));
    }

    @GetMapping("/recent")
    public ResponseEntity<?> getCouponRecentHistory(
            @AuthJwtPrincipal JwtPrincipal jwtPrincipal
    ) {
        List<CouponResponse> couponResponses = couponService.getRecentHistory(Long.valueOf(jwtPrincipal.getMemberId()));
        if (couponResponses.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(couponResponses);
    }

    @PostMapping("/register")
    public ResponseEntity<Void> registerCoupon(
            @Valid @RequestBody CouponRegisterRequest couponRegisterRequest,
            @AuthJwtPrincipal JwtPrincipal jwtPrincipal
    ) {
        couponService.register(Long.valueOf(jwtPrincipal.getMemberId()), couponRegisterRequest);
        return ResponseEntity.created(URI.create("/")).build();
    }

    @PutMapping("/{coupon_number}")
    public ResponseEntity<Void> updateCoupon(
            @PathVariable("coupon_number") String couponNumber,
            @Valid @RequestBody CouponUpdateRequest couponUpdateRequest,
            @AuthJwtPrincipal JwtPrincipal jwtPrincipal) {
        couponService.updateCoupon(
                Long.valueOf(jwtPrincipal.getMemberId()), couponNumber, couponUpdateRequest);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{coupon_number}/expose")
    public ResponseEntity<Void> exposeCoupon(
            @PathVariable("coupon_number") String couponNumber,
            @Valid @RequestBody CouponExposeRequest couponExposeRequest,
            @AuthJwtPrincipal JwtPrincipal jwtPrincipal
    ) {
        couponService.exposeCoupon(
                Long.valueOf(jwtPrincipal.getMemberId()), couponNumber, couponExposeRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{coupon_number}")
    public ResponseEntity<Void> deleteCoupon(
            @PathVariable("coupon_number") String couponNumber,
            @AuthJwtPrincipal JwtPrincipal jwtPrincipal
    ) {
        couponService.deleteCoupon(Long.valueOf(jwtPrincipal.getMemberId()), couponNumber);
        return ResponseEntity.ok().build();
    }
}
