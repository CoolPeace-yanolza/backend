package com.coolpeace.domain.coupon.repository;

import com.coolpeace.domain.accommodation.entity.Accommodation;
import com.coolpeace.domain.coupon.dto.request.SearchCouponParams;
import com.coolpeace.domain.coupon.dto.request.type.SearchCouponDateFilterType;
import com.coolpeace.domain.coupon.dto.request.type.SearchCouponStatusFilterType;
import com.coolpeace.domain.coupon.entity.Coupon;
import com.coolpeace.domain.coupon.entity.type.CouponStatusType;
import com.coolpeace.global.common.ValuedEnum;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.support.PageableExecutionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.coolpeace.domain.coupon.entity.QCoupon.coupon;
import static com.coolpeace.domain.coupon.entity.QCouponRooms.couponRooms;
import static com.coolpeace.domain.coupon.entity.type.CouponStatusType.*;
import static com.coolpeace.domain.room.entity.QRoom.room;

public class CouponRepositoryImpl extends QuerydslRepositorySupport implements CouponRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public CouponRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        super(Coupon.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Page<Coupon> findAllCoupons(Long memberId, Long accommodationId, SearchCouponParams params, PageRequest pageable) {
        // 검색 필터링
        BooleanBuilder searchCouponPredicate = new BooleanBuilder(coupon.member.id.eq(memberId));

        // 쿠폰 상태
        if (params.status() != null) {
            searchCouponPredicate.and(switch (ValuedEnum.of(SearchCouponStatusFilterType.class, params.status())) {
                case EXPOSURE_ON -> coupon.couponStatus.eq(EXPOSURE_ON);
                case EXPOSURE_OFF -> coupon.couponStatus.eq(EXPOSURE_OFF)
                        .or(coupon.couponStatus.eq(EXPOSURE_WAIT));
                case EXPIRED -> coupon.couponStatus.eq(EXPOSURE_END);
                case All -> coupon.couponStatus.ne(DELETED);
            });
        } else {
            searchCouponPredicate.and(coupon.couponStatus.ne(DELETED));
        }

        // 쿠폰 이름
        if (params.title() != null) {
            searchCouponPredicate.and(coupon.title.contains(params.title()));
        }

        // 쿠폰 날짜
        if (params.date() != null) {
            searchCouponPredicate.and(switch (ValuedEnum.of(SearchCouponDateFilterType.class, params.date())) {
                case THREE_MONTHS -> coupon.createdAt.after(LocalDateTime.now().minusMonths(3));
                case SIX_MONTHS -> coupon.createdAt.after(LocalDateTime.now().minusMonths(6));
                case YEAR -> coupon.createdAt.after(LocalDateTime.now().minusYears(1));
            });
        } else {
            searchCouponPredicate.and(coupon.createdAt.after(LocalDateTime.now().minusYears(1)));
        }

        JPAQuery<Coupon> couponJPAQuery = jpaQueryFactory.selectFrom(coupon)
                .leftJoin(coupon.couponRooms, couponRooms).fetchJoin()
                .leftJoin(couponRooms.room, room).fetchJoin()
                .where(coupon.accommodation.id.eq(accommodationId))
                .where(searchCouponPredicate);

        JPAQuery<Long> totalQuery = jpaQueryFactory.select(coupon.count()).from(coupon)
                .where(coupon.accommodation.id.eq(accommodationId))
                .where(searchCouponPredicate);

        List<Coupon> coupons = Objects.requireNonNull(getQuerydsl()).applyPagination(pageable, couponJPAQuery).fetch();
        return PageableExecutionUtils.getPage(coupons, pageable, totalQuery::fetchOne);
    }

    @Override
    public Map<CouponStatusType, Long> countCouponsByCouponStatus(Long memberId, Long accommodationId) {

        List<Tuple> results = jpaQueryFactory
                .select(coupon.couponStatus, coupon.count())
                .where(coupon.member.id.eq(memberId)
                        .and(coupon.accommodation.id.eq(accommodationId)))
                .from(coupon)
                .groupBy(coupon.couponStatus)
                .fetch();

        return results.stream()
                .collect(Collectors.toMap(
                        tuple -> tuple.get(coupon.couponStatus),
                        tuple -> Optional.ofNullable(tuple.get(coupon.count())).orElse(0L)
                ));
    }

    @Override
    public List<Coupon> findRecentCouponByMemberId(Long memberId) {
        return jpaQueryFactory.selectFrom(coupon)
                .leftJoin(coupon.couponRooms, couponRooms).fetchJoin()
                .leftJoin(couponRooms.room, room).fetchJoin()
                .where(coupon.member.id.eq(memberId),
                        coupon.couponStatus.eq(EXPOSURE_END))
                .orderBy(coupon.createdAt.desc())
                .limit(6)
                .fetch();
    }

    @Override
    public Optional<Coupon> findByCouponNumber(String couponNumber) {
        List<Coupon> coupons = jpaQueryFactory.selectFrom(coupon)
                .leftJoin(coupon.couponRooms, couponRooms).fetchJoin()
                .leftJoin(couponRooms.room, room).fetchJoin()
                .where(coupon.couponNumber.eq(couponNumber))
                .fetch();
        return Optional.ofNullable(coupons.get(0));
    }

    @Override
    public List<Coupon> exposureCoupons(Long memberId, Long accommodationId) {
        return jpaQueryFactory.selectFrom(coupon)
                .where(coupon.member.id.eq(memberId)
                        .and(coupon.accommodation.id.eq(accommodationId))
                        .and(coupon.couponStatus.eq(EXPOSURE_ON)))
                .fetch();
    }

    @Override
    public Boolean noRegister(Long memberId, Long accommodationId) {

        return jpaQueryFactory.selectFrom(coupon)
                .where(coupon.member.id.eq(memberId)
                        .and(coupon.accommodation.id.eq(accommodationId))
                        .and(coupon.couponStatus.ne(DELETED)))
                .fetch().isEmpty();
    }

    @Override
    public Boolean noExposure(Long memberId, Long accommodationId) {

        return jpaQueryFactory.selectFrom(coupon)
                .where(coupon.member.id.eq(memberId)
                        .and(coupon.accommodation.id.eq(accommodationId))
                        .and(coupon.couponStatus.eq(EXPOSURE_ON)))
                .fetch().isEmpty();
    }


    @Override
    public List<Coupon> expiration3days(Long memberId, Long accommodationId) {
        return jpaQueryFactory.selectFrom(coupon)
                .where(coupon.member.id.eq(memberId)
                        .and(coupon.accommodation.id.eq(accommodationId))
                        .and(coupon.couponStatus.ne(DELETED))
                    .and(coupon.exposureEndDate.between(LocalDate.now().minusDays(1),
                        LocalDate.now().plusDays(4))))
                .fetch();
    }

    @Override
    public List<Coupon> findAllByExposureDate(Accommodation accommodation, LocalDate localDate) {
        return jpaQueryFactory.selectFrom(coupon)
            .where(coupon.accommodation.eq(accommodation)
                .and(coupon.exposureStartDate.before(localDate))
                .and(coupon.exposureEndDate.after(localDate))).fetch();
    }

    @Override
    public List<Coupon> endExposureCoupons(LocalDate nowDate) {
        return jpaQueryFactory.selectFrom(coupon)
            .where(coupon.couponStatus.ne(CouponStatusType.DELETED)
                .and(coupon.exposureEndDate.before(nowDate))).fetch();
    }

    @Override
    public List<Coupon> startExposureCoupons(LocalDate nowDate) {
        return jpaQueryFactory.selectFrom(coupon)
            .where(coupon.couponStatus.eq(CouponStatusType.EXPOSURE_WAIT)
                .and((coupon.exposureStartDate.after(nowDate))
                    .or(coupon.exposureStartDate.eq(nowDate)))).fetch();
    }
}
