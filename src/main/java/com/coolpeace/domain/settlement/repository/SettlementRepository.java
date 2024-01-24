package com.coolpeace.domain.settlement.repository;

import com.coolpeace.domain.accommodation.entity.Accommodation;
import com.coolpeace.domain.data.dto.request.SettlementStatistic;
import com.coolpeace.domain.settlement.entity.Settlement;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.PathVariable;

public interface SettlementRepository extends JpaRepository<Settlement, Long>,SettlementRepositoryCustom {
    Page<Settlement> findAllByAccommodationAndCouponUseDateGreaterThanEqualAndCouponUseDateLessThanEqualOrderBySumPriceDesc
        (Pageable pageable, Accommodation accommodation, LocalDate startDate, LocalDate endDate);
    Page<Settlement> findAllByAccommodationAndCouponUseDateGreaterThanEqualAndCouponUseDateLessThanEqualOrderByCompleteAtDesc
        (Pageable pageable, Accommodation accommodation, LocalDate startDate, LocalDate endDate);
    Page<Settlement> findAllByAccommodationAndCouponUseDateGreaterThanEqualAndCouponUseDateLessThanEqualOrderByCouponCountDesc
        (Pageable pageable, Accommodation accommodation, LocalDate startDate, LocalDate endDate);
    Page<Settlement> findAllByAccommodationAndCouponUseDateGreaterThanEqualAndCouponUseDateLessThanEqualOrderByCouponUseDateDesc
        (Pageable pageable, Accommodation accommodation, LocalDate startDate, LocalDate endDate);

    @Query(value = "select rr.coupon_id as couponId, "
        + "       a.id as accommodationId, "
        + "       date_format(r.start_date, '%Y-%m-%d') as couponUseDate, "
        + "       date_format(date_add(r.start_date, INTERVAL 1 DAY), '%Y-%m-%d') as completeAt, "
        + "       count(*) as count, "
        + "       sum(if(r.reservation_status = 'CANCELLED', r.discount_price, 0)) as cancelPrice, "
        + "       sum(r.discount_price) as discountPrice "
        + "from reservation r, "
        + "    room_reservation rr, "
        + "    room ro, "
        + "    accommodation a "
        + "where r.id = rr.reservation_id "
        + "and rr.room_id = ro.id "
        + "and ro.accommodation_id = a.id "
        + "and r.start_date between date_format(:start, '%Y-%m-%d') and date_format(:end, '%Y-%m-%d') "
        + "group by coupon_id, accommodation_id, start_date "
        + "order by couponId, accommodationId, couponUseDate", nativeQuery = true)
    List<SettlementStatistic> statisticReservation(@PathVariable("start") LocalDate start, @PathVariable("end") LocalDate end);

}
