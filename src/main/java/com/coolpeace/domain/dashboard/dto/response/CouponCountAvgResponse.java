package com.coolpeace.domain.dashboard.dto.response;

public record CouponCountAvgResponse(
    String address,
    String couponAvg
) {
    public static CouponCountAvgResponse from(int couponCount, int accommodationCount,
        String address) {
        String couponAvg = "0";
        if(accommodationCount!=0){
            couponAvg = String.format("%.1f", ((double)couponCount / accommodationCount));
        }
        return new CouponCountAvgResponse(address, couponAvg);
    }
}
