package com.coolpeace.domain.dashboard.dto.response;

import com.coolpeace.domain.statistics.entity.LocalCouponDownload;

public record MonthlyCouponDownloadResponse(
    String firstCouponTitle,
    String secondCouponTitle,
    String thirdCouponTitle
) {

    public static MonthlyCouponDownloadResponse from(LocalCouponDownload localCouponDownload) {
        return new MonthlyCouponDownloadResponse(
            localCouponDownload.getFirstCouponTitle(),
            localCouponDownload.getSecondCouponTitle(),
            localCouponDownload.getThirdCouponTitle()
        );
    }

}
