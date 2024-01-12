package com.coolpeace.core.domain.coupon.repository;

import com.coolpeace.core.domain.coupon.entity.Coupon;
import com.coolpeace.core.domain.coupon.entity.CouponRooms;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CouponRoomsRepository extends JpaRepository<CouponRooms,Long> {
    List<CouponRooms> findByCoupon(Coupon coupon);
}
