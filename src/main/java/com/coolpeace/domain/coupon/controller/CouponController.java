package com.coolpeace.domain.coupon.controller;

import com.coolpeace.domain.coupon.dto.request.CouponExposeRequest;
import com.coolpeace.domain.coupon.dto.request.CouponRegisterRequest;
import com.coolpeace.domain.coupon.dto.request.CouponUpdateRequest;
import com.coolpeace.domain.coupon.dto.request.SearchCouponParams;
import com.coolpeace.domain.coupon.dto.response.CouponRegisterResponse;
import com.coolpeace.domain.coupon.dto.response.CouponResponse;
import com.coolpeace.domain.coupon.service.CouponService;
import com.coolpeace.global.jwt.security.JwtPrincipal;
import com.coolpeace.global.resolver.AuthJwtPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/v1/coupons")
@RequiredArgsConstructor
public class CouponController {
    private final CouponService couponService;

    @GetMapping
    public ResponseEntity<Page<CouponResponse>> searchCoupons(
            SearchCouponParams searchCouponParams,
            @PageableDefault(size = 6) Pageable pageable,
            @AuthJwtPrincipal JwtPrincipal jwtPrincipal
    ) {
        return ResponseEntity.ok(couponService.searchCoupons(
                Long.valueOf(jwtPrincipal.getMemberId()), searchCouponParams, pageable));
    }

    @GetMapping("/recent")
    public ResponseEntity<?> getCouponRecentHistory(
            @AuthJwtPrincipal JwtPrincipal jwtPrincipal
    ) {
        Optional<CouponResponse> couponResponse = couponService.getRecentHistory(Long.valueOf(jwtPrincipal.getMemberId()));
        if (couponResponse.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(couponResponse.get());
    }

    @PostMapping("/register")
    public ResponseEntity<CouponRegisterResponse> registerCoupon(
            @Valid @RequestBody CouponRegisterRequest couponRegisterRequest,
            @AuthJwtPrincipal JwtPrincipal jwtPrincipal
    ) {
        couponService.register(Long.valueOf(jwtPrincipal.getMemberId()), couponRegisterRequest);
        return ResponseEntity.created(URI.create("/")).build();
    }

    @PutMapping("/{coupon_id}")
    public ResponseEntity<Void> updateCoupon(
            @PathVariable("coupon_id") String couponId,
            @Valid @RequestBody CouponUpdateRequest couponUpdateRequest) {
        couponService.updateCoupon(couponId, couponUpdateRequest);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{coupon_id}/expose")
    public ResponseEntity<Void> exposeCoupon(
            @PathVariable("coupon_id") String couponId,
            @Valid @RequestBody CouponExposeRequest couponExposeRequest
    ) {
        couponService.exposeCoupon(couponId, couponExposeRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{coupon_id}")
    public ResponseEntity<Void> deleteCoupon(
            @PathVariable("coupon_id") String couponId,
            @AuthJwtPrincipal JwtPrincipal jwtPrincipal
    ) {
        couponService.deleteCoupon(Long.valueOf(jwtPrincipal.getMemberId()), Long.valueOf(couponId));
        return ResponseEntity.ok().build();
    }
}
