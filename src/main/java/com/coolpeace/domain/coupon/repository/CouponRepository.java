package com.coolpeace.domain.coupon.repository;

import com.coolpeace.domain.accommodation.entity.Accommodation;
import com.coolpeace.domain.coupon.entity.Coupon;
import com.coolpeace.domain.member.entity.Member;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon,Long>,CouponRepositoryCustom {

    List<Coupon> findAllByMemberAndAccommodation(Member member, Accommodation accommodation);
}
