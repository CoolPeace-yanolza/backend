package com.coolpeace.domain.coupon.controller;

import com.coolpeace.domain.coupon.dto.request.CouponExposeRequest;
import com.coolpeace.domain.coupon.dto.request.CouponRegisterRequest;
import com.coolpeace.domain.coupon.dto.request.CouponUpdateRequest;
import com.coolpeace.domain.coupon.dto.response.CouponRecentHistoryResponse;
import com.coolpeace.domain.coupon.dto.response.CouponRegisterResponse;
import com.coolpeace.domain.coupon.dto.response.CouponSearchResponse;
import com.coolpeace.domain.coupon.service.CouponService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;

@RestController
@RequestMapping("/v1/coupons")
@RequiredArgsConstructor
public class CouponController {
    private final CouponService couponService;

    @GetMapping
    public ResponseEntity<CouponSearchResponse> searchCoupons(
            @PageableDefault Pageable pageable
    ) {
        return ResponseEntity.ok(CouponSearchResponse.from(couponService.searchCoupons()));
    }

    @GetMapping("/recent")
    public ResponseEntity<CouponRecentHistoryResponse> getCouponRecentHistory() {
        return ResponseEntity.ok(CouponRecentHistoryResponse.from(couponService.getRecentHistory()));
    }

    @PostMapping("/register")
    public ResponseEntity<CouponRegisterResponse> registerCoupon(
            @Valid @RequestBody CouponRegisterRequest couponRegisterRequest
    ) {
        return ResponseEntity.created(URI.create("/"))
                .body(couponService.register(couponRegisterRequest));
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
            @PathVariable("coupon_id") String couponId
    ) {
        couponService.deleteCoupon(couponId);
        return ResponseEntity.ok().build();
    }
}
