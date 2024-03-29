package com.coolpeace.domain.dashboard.controller;

import com.coolpeace.domain.coupon.service.CouponQueryService;
import com.coolpeace.domain.dashboard.service.DashboardService;
import com.coolpeace.global.jwt.security.JwtPrincipal;
import com.coolpeace.global.resolver.AuthJwtPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("v1/dashboards")
public class DashboardController {

    private final DashboardService dashboardService;
    private final CouponQueryService couponQueryService;

    @GetMapping("/{accommodation_id}/reports/month")
    public ResponseEntity<?> monthlyData(@PathVariable("accommodation_id") Long accommodationId,
     @AuthJwtPrincipal JwtPrincipal jwtPrincipal) {
        return ResponseEntity.ok().body(dashboardService
            .monthlyData(jwtPrincipal.getMemberId(), accommodationId));
    }

    @GetMapping("/{accommodation_id}/reports/week")
    public ResponseEntity<?> weeklyCoupon(@PathVariable("accommodation_id") Long accommodationId,
        @AuthJwtPrincipal JwtPrincipal jwtPrincipal){
        return ResponseEntity.ok().body(dashboardService
            .weeklyCoupon(jwtPrincipal.getMemberId(), accommodationId));
    }

    @GetMapping("/{accommodation_id}/reports/daily")
    public ResponseEntity<?> dailyCouponReport(@PathVariable("accommodation_id") Long accommodationId,
        @AuthJwtPrincipal JwtPrincipal jwtPrincipal){
        return ResponseEntity.ok().body(couponQueryService
            .dailyReport(jwtPrincipal.getMemberId(), accommodationId));
    }

    @GetMapping("/{accommodation_id}/coupons/local/download")
    public ResponseEntity<?> downloadCouponTop3(@PathVariable("accommodation_id") Long accommodationId,
        @AuthJwtPrincipal JwtPrincipal jwtPrincipal){
        return ResponseEntity.ok().body(dashboardService
            .downloadCouponTop3(jwtPrincipal.getMemberId(), accommodationId));
    }

    @GetMapping("/{accommodation_id}/coupons/local/count")
    public ResponseEntity<?> couponCountAvg(@PathVariable("accommodation_id") Long accommodationId,
        @AuthJwtPrincipal JwtPrincipal jwtPrincipal){
        return ResponseEntity.ok().body(dashboardService
            .couponCountAvg(jwtPrincipal.getMemberId(), accommodationId));
    }
    
    @GetMapping("/{accommodation_id}/reports/year")
    public ResponseEntity<?> byYearCumulativeData(@PathVariable("accommodation_id") Long accommodationId,
        @AuthJwtPrincipal JwtPrincipal jwtPrincipal,
        @RequestParam int year){
        return ResponseEntity.ok().body(dashboardService
            .byYearCumulativeData(year, jwtPrincipal.getMemberId(), accommodationId));
    }

    @GetMapping("/{accommodation_id}/reports/total")
    public ResponseEntity<?> cumulativeData(@PathVariable("accommodation_id") Long accommodationId,
        @AuthJwtPrincipal JwtPrincipal jwtPrincipal){
        return ResponseEntity.ok().body(dashboardService
            .cumulativeData(jwtPrincipal.getMemberId(), accommodationId));
    }
}
