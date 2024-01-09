package com.coolpeace.api.domain.coupon.repository;

import com.coolpeace.api.domain.accommodation.entity.Accommodation;
import com.coolpeace.api.domain.coupon.entity.Coupon;
import com.coolpeace.api.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CouponRepository extends JpaRepository<Coupon,Long>,CouponRepositoryCustom {
    boolean existsByMemberIdAndCouponNumber(Long memberId, String couponNumber);
    List<Coupon> findAllByMemberAndAccommodation(Member member, Accommodation accommodation);
}
