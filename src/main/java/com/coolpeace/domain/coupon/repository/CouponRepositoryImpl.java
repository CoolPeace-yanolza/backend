package com.coolpeace.domain.coupon.repository;

import static com.coolpeace.domain.coupon.entity.QCoupon.coupon;

import com.coolpeace.domain.accommodation.entity.Accommodation;
import com.coolpeace.domain.coupon.entity.Coupon;
import com.coolpeace.domain.coupon.entity.CouponStatus;
import com.coolpeace.domain.member.entity.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

public class CouponRepositoryImpl implements CouponRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    public CouponRepositoryImpl(EntityManager entityManager) {
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }


    @Override
    public List<Coupon> exposureCoupons(Long memberId, Long accommodationId) {
        return jpaQueryFactory.selectFrom(coupon)
            .where(coupon.member.id.eq(memberId)
                .and(coupon.accommodation.id.eq(accommodationId))
                .and(coupon.couponStatus.eq(CouponStatus.EXPOSURE_ON)))
            .fetch();
    }

    @Override
    public Boolean noRegister(Long memberId, Long accommodationId) {

        return jpaQueryFactory.selectFrom(coupon)
            .where(coupon.member.id.eq(memberId)
                .and(coupon.accommodation.id.eq(accommodationId))
                .and(coupon.couponStatus.ne(CouponStatus.DELETED)))
            .fetch().isEmpty();
    }

    @Override
    public Boolean noExposure(Long memberId, Long accommodationId) {

        return jpaQueryFactory.selectFrom(coupon)
            .where(coupon.member.id.eq(memberId)
                .and(coupon.accommodation.id.eq(accommodationId))
                .and(coupon.couponStatus.eq(CouponStatus.EXPOSURE_ON)))
            .fetch().isEmpty();
    }


    @Override
    public List<Coupon> expiration3days(Long memberId, Long accommodationId) {
        return jpaQueryFactory.selectFrom(coupon)
            .where(coupon.member.id.eq(memberId)
                .and(coupon.accommodation.id.eq(accommodationId))
                .and(coupon.couponStatus.eq(CouponStatus.EXPOSURE_ON))
                .and(coupon.endDate.before(LocalDateTime.now().plusDays(3))))
            .fetch();
    }
}
