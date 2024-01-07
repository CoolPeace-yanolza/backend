package com.coolpeace.domain.coupon.repository;

import com.coolpeace.domain.coupon.entity.Coupon;
import com.coolpeace.domain.coupon.entity.CouponRooms;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CouponRoomsRepository extends JpaRepository<CouponRooms,Long> {
    List<CouponRooms> findByCoupon(Coupon coupon);
}
