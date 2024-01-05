package com.coolpeace.domain.coupon.repository;

import com.coolpeace.domain.coupon.dto.request.SearchCouponParams;
import com.coolpeace.domain.coupon.entity.Coupon;
import com.coolpeace.domain.coupon.entity.type.CouponStatusType;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.support.PageableExecutionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static com.coolpeace.domain.coupon.entity.QCoupon.coupon;

public class CouponRepositoryImpl extends QuerydslRepositorySupport implements CouponRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public CouponRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        super(Coupon.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Page<Coupon> findAllCoupons(Long memberId, SearchCouponParams params, PageRequest pageable) {
        // 검색 필터링
        BooleanBuilder searchCouponPredicate = new BooleanBuilder(coupon.member.id.eq(memberId));

        // 쿠폰 상태
        if (params.status() != null) {
            searchCouponPredicate.and(switch (params.status()) {
                case EXPOSURE_ON -> coupon.couponStatus.eq(CouponStatusType.EXPOSURE_ON);
                case EXPOSURE_OFF -> coupon.couponStatus.eq(CouponStatusType.EXPOSURE_OFF);
                case EXPIRED -> coupon.couponStatus.eq(CouponStatusType.EXPOSURE_END)
                        .or(coupon.couponStatus.eq(CouponStatusType.DELETED));
                case All -> coupon.couponStatus.isNotNull();
            });
        }

        // 쿠폰 이름
        if (params.title() != null) {
            searchCouponPredicate.and(coupon.title.like(params.title()));
        }

        // 쿠폰 날짜
        if (params.date() != null) {
            searchCouponPredicate.and(switch (params.date()) {
                case THREE_MONTHS -> coupon.createdAt.after(LocalDateTime.now().minusMonths(3));
                case SIX_MONTHS -> coupon.createdAt.after(LocalDateTime.now().minusMonths(6));
                case YEAR -> coupon.createdAt.after(LocalDateTime.now().minusYears(1));
            });
        }

        JPAQuery<Coupon> couponJPAQuery = jpaQueryFactory.selectFrom(coupon)
                .leftJoin(coupon.accommodation)
                .leftJoin(coupon.room)
                .where(searchCouponPredicate);

        JPAQuery<Long> totalQuery = jpaQueryFactory.select(coupon.count())
                .from(coupon)
                .leftJoin(coupon.accommodation)
                .leftJoin(coupon.room)
                .where(searchCouponPredicate);

        List<Coupon> coupons = Objects.requireNonNull(getQuerydsl()).applyPagination(pageable, couponJPAQuery).fetch();
        return PageableExecutionUtils.getPage(coupons, pageable, totalQuery::fetchOne);
    }

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
                        .and(coupon.exposureEndDate.before(LocalDate.now().plusDays(3))))
                .fetch();
    }
}
