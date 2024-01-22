package com.coolpeace.domain.coupon.controller;

import com.coolpeace.domain.coupon.dto.request.CouponExposeRequest;
import com.coolpeace.domain.coupon.dto.request.CouponRegisterRequest;
import com.coolpeace.domain.coupon.dto.request.CouponUpdateRequest;
import com.coolpeace.domain.coupon.dto.response.CouponResponse;
import com.coolpeace.domain.coupon.service.CouponService;
import com.coolpeace.global.security.MemberPrincipal;
import com.coolpeace.global.resolver.AuthJwtPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/v1/coupons")
@RequiredArgsConstructor
public class CouponController {
    private final CouponService couponService;

    @GetMapping("/{coupon_number}")
    public ResponseEntity<CouponResponse> getCouponByCouponNumber(
            @PathVariable("coupon_number") String couponNumber,
            @AuthJwtPrincipal MemberPrincipal memberPrincipal) {
        return ResponseEntity.ok(CouponResponse.from(couponService.getCouponByCouponNumber(
                Long.valueOf(memberPrincipal.getMemberId()), couponNumber)));
    }

    @GetMapping("/recent")
    public ResponseEntity<?> getCouponRecentHistory(
            @AuthJwtPrincipal MemberPrincipal memberPrincipal
    ) {
        List<CouponResponse> couponResponses = couponService.getRecentHistory(Long.valueOf(memberPrincipal.getMemberId()));
        if (couponResponses.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(couponResponses);
    }

    @PostMapping("/register")
    public ResponseEntity<Void> registerCoupon(
            @Valid @RequestBody CouponRegisterRequest couponRegisterRequest,
            @AuthJwtPrincipal MemberPrincipal memberPrincipal
    ) {
        couponService.register(Long.valueOf(memberPrincipal.getMemberId()), couponRegisterRequest);
        return ResponseEntity.created(URI.create("/")).build();
    }

    @PutMapping("/{coupon_number}")
    public ResponseEntity<Void> updateCoupon(
            @PathVariable("coupon_number") String couponNumber,
            @Valid @RequestBody CouponUpdateRequest couponUpdateRequest,
            @AuthJwtPrincipal MemberPrincipal memberPrincipal) {
        couponService.updateCoupon(
                Long.valueOf(memberPrincipal.getMemberId()), couponNumber, couponUpdateRequest);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{coupon_number}/expose")
    public ResponseEntity<Void> exposeCoupon(
            @PathVariable("coupon_number") String couponNumber,
            @Valid @RequestBody CouponExposeRequest couponExposeRequest,
            @AuthJwtPrincipal MemberPrincipal memberPrincipal
    ) {
        couponService.exposeCoupon(
                Long.valueOf(memberPrincipal.getMemberId()), couponNumber, couponExposeRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{coupon_number}")
    public ResponseEntity<Void> deleteCoupon(
            @PathVariable("coupon_number") String couponNumber,
            @AuthJwtPrincipal MemberPrincipal memberPrincipal
    ) {
        couponService.deleteCoupon(Long.valueOf(memberPrincipal.getMemberId()), couponNumber);
        return ResponseEntity.ok().build();
    }
}
