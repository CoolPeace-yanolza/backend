package com.coolpeace.domain.coupon.controller;

import com.coolpeace.domain.coupon.dto.request.SearchCouponParams;
import com.coolpeace.domain.coupon.dto.response.CouponCategoryResponse;
import com.coolpeace.domain.coupon.dto.response.CouponResponse;
import com.coolpeace.domain.coupon.dto.response.CouponSearchResponse;
import com.coolpeace.domain.coupon.service.CouponService;
import com.coolpeace.global.security.MemberPrincipal;
import com.coolpeace.global.resolver.AuthJwtPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CouponSearchController {
    private final CouponService couponService;

    @GetMapping("/v1/accommodations/{accommodation_id}/coupons")
    public ResponseEntity<CouponSearchResponse> searchCoupons(
            @PathVariable("accommodation_id") Long accommodationId,
            @Valid SearchCouponParams searchCouponParams,
            @PageableDefault Pageable pageable,
            @AuthJwtPrincipal MemberPrincipal memberPrincipal
    ) {
        Page<CouponResponse> couponResponses = couponService.searchCoupons(
                Long.valueOf(memberPrincipal.getMemberId()), accommodationId, searchCouponParams, pageable);
        CouponCategoryResponse categoryResponse = couponService.getCouponCategories();
        return ResponseEntity.ok(CouponSearchResponse.from(couponResponses, categoryResponse));
    }
}
