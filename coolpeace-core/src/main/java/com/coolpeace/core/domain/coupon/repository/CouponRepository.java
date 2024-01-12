package com.coolpeace.core.domain.coupon.repository;

import com.coolpeace.core.domain.accommodation.entity.Accommodation;
import com.coolpeace.core.domain.coupon.entity.Coupon;
import com.coolpeace.core.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CouponRepository extends JpaRepository<Coupon,Long>,CouponRepositoryCustom {
    boolean existsByMemberIdAndCouponNumber(Long memberId, String couponNumber);
    List<Coupon> findAllByAccommodation(Accommodation accommodation);
}
