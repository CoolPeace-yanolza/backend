package com.coolpeace.domain.settlement.repository;

import com.coolpeace.domain.accommodation.entity.Accommodation;
import com.coolpeace.domain.settlement.entity.Settlement;
import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettlementRepository extends JpaRepository<Settlement, Long>,SettlementRepositoryCustom {
    Page<Settlement> findAllByAccommodationAndCouponUseDateGreaterThanEqualAndCouponUseDateLessThanEqualOrderBySumPriceDesc
        (Pageable pageable, Accommodation accommodation, LocalDate startDate, LocalDate endDate);
    Page<Settlement> findAllByAccommodationAndCouponUseDateGreaterThanEqualAndCouponUseDateLessThanEqualOrderByCompleteAtDesc
        (Pageable pageable, Accommodation accommodation, LocalDate startDate, LocalDate endDate);
    Page<Settlement> findAllByAccommodationAndCouponUseDateGreaterThanEqualAndCouponUseDateLessThanEqualOrderByCouponCountDesc
        (Pageable pageable, Accommodation accommodation, LocalDate startDate, LocalDate endDate);
    Page<Settlement> findAllByAccommodationAndCouponUseDateGreaterThanEqualAndCouponUseDateLessThanEqualOrderByCouponUseDateDesc
        (Pageable pageable, Accommodation accommodation, LocalDate startDate, LocalDate endDate);

}
