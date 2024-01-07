package com.coolpeace.domain.coupon.repository;

import com.coolpeace.domain.accommodation.entity.Accommodation;
import com.coolpeace.domain.coupon.entity.Coupon;
import com.coolpeace.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CouponRepository extends JpaRepository<Coupon,Long>,CouponRepositoryCustom {
    boolean existsByMemberIdAndCouponNumber(Long memberId, String couponNumber);
    List<Coupon> findAllByMemberAndAccommodation(Member member, Accommodation accommodation);
}
