package com.coolpeace.api.domain.coupon.entity.key;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@Embeddable
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CouponRoomsId implements Serializable {
    private Long couponId;
    private Long roomId;

    public static CouponRoomsId from(Long couponId, Long accommodationId) {
        return new CouponRoomsId(couponId, accommodationId);
    }
}
