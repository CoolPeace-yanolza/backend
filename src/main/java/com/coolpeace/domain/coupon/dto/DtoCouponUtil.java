package com.coolpeace.domain.coupon.dto;

import com.coolpeace.domain.coupon.dto.request.type.DtoCouponUseDayOfWeekType;
import com.coolpeace.domain.coupon.dto.request.type.DtoCouponUseDaysType;
import com.coolpeace.domain.coupon.entity.type.CouponUseDaysType;
public class DtoCouponUtil {

    public static DtoCouponUseDaysType filteringCouponUseConditionDays(CouponUseDaysType couponUseDays) {
        if (couponUseDays.isOneDay()) {
            return DtoCouponUseDaysType.ONEDAY;
        }
        return DtoCouponUseDaysType.valueOf(couponUseDays.name());
    }

    public static DtoCouponUseDayOfWeekType filteringCouponUseConditionDayOfWeek(CouponUseDaysType couponUseDays) {
        if (couponUseDays.isOneDay()) {
            return DtoCouponUseDayOfWeekType.valueOf(couponUseDays.name());
        }
        return null;
    }
}
