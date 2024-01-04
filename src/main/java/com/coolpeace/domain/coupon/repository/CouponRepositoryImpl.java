package com.coolpeace.domain.coupon.repository;

import com.coolpeace.domain.coupon.entity.Coupon;
import com.coolpeace.domain.coupon.entity.CouponStatusType;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import static com.coolpeace.domain.coupon.entity.QCoupon.coupon;

@RequiredArgsConstructor
public class CouponRepositoryImpl implements CouponRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Coupon> exposureCoupons(Long memberId, Long accommodationId) {
        return jpaQueryFactory.selectFrom(coupon)
            .where(coupon.member.id.eq(memberId)
                .and(coupon.accommodation.id.eq(accommodationId))
                .and(coupon.couponStatus.eq(CouponStatusType.EXPOSURE_ON)))
            .fetch();
    }

    @Override
    public Boolean noRegister(Long memberId, Long accommodationId) {

        return jpaQueryFactory.selectFrom(coupon)
            .where(coupon.member.id.eq(memberId)
                .and(coupon.accommodation.id.eq(accommodationId))
                .and(coupon.couponStatus.ne(CouponStatusType.DELETED)))
            .fetch().isEmpty();
    }

    @Override
    public Boolean noExposure(Long memberId, Long accommodationId) {

        return jpaQueryFactory.selectFrom(coupon)
            .where(coupon.member.id.eq(memberId)
                .and(coupon.accommodation.id.eq(accommodationId))
                .and(coupon.couponStatus.eq(CouponStatusType.EXPOSURE_ON)))
            .fetch().isEmpty();
    }


    @Override
    public List<Coupon> expiration3days(Long memberId, Long accommodationId) {
        return jpaQueryFactory.selectFrom(coupon)
            .where(coupon.member.id.eq(memberId)
                .and(coupon.accommodation.id.eq(accommodationId))
                .and(coupon.couponStatus.eq(CouponStatusType.EXPOSURE_ON))
                .and(coupon.exposeEndDate.before(LocalDateTime.now().plusDays(3))))
            .fetch();
    }
}
