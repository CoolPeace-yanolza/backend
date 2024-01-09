package com.coolpeace.api.domain.coupon.controller;

import com.coolpeace.api.domain.coupon.dto.request.CouponExposeRequest;
import com.coolpeace.api.domain.coupon.dto.request.CouponRegisterRequest;
import com.coolpeace.api.domain.coupon.dto.request.CouponUpdateRequest;
import com.coolpeace.api.domain.coupon.dto.request.SearchCouponParams;
import com.coolpeace.api.domain.coupon.dto.response.CouponResponse;
import com.coolpeace.api.domain.coupon.service.CouponService;
import com.coolpeace.api.global.jwt.security.JwtPrincipal;
import com.coolpeace.api.global.resolver.AuthJwtPrincipal;
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
