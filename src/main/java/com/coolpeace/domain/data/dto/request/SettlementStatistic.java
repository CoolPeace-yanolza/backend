package com.coolpeace.domain.data.dto.request;

import java.time.LocalDate;

public interface SettlementStatistic {

    Long getCouponId();

    Long getAccommodationId();

    LocalDate getCouponUseDate();

    LocalDate getCompleteAt();

    Integer getCount();

    Integer getCancelPrice();

    Integer getDiscountPrice();

}
