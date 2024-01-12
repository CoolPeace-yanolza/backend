package com.coolpeace.core.domain.settlement.repository;

import com.coolpeace.core.domain.accommodation.entity.Accommodation;
import com.coolpeace.core.domain.settlement.entity.Settlement;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettlementRepository extends JpaRepository<Settlement, Long>,SettlementRepositoryCustom {
    Page<Settlement> findAllByAccommodationAndCouponUseDateAfterAndCouponUseDateBeforeOrderBySumPriceDesc
        (Pageable pageable, Accommodation accommodation, LocalDate startDate, LocalDate endDate);
    Page<Settlement> findAllByAccommodationAndCouponUseDateAfterAndCouponUseDateBeforeOrderByCompleteAtDesc
        (Pageable pageable, Accommodation accommodation, LocalDate startDate, LocalDate endDate);
    Page<Settlement> findAllByAccommodationAndCouponUseDateAfterAndCouponUseDateBeforeOrderByCouponCountDesc
        (Pageable pageable, Accommodation accommodation, LocalDate startDate, LocalDate endDate);
    Page<Settlement> findAllByAccommodationAndCouponUseDateAfterAndCouponUseDateBeforeOrderByCouponUseDateDesc
        (Pageable pageable, Accommodation accommodation, LocalDate startDate, LocalDate endDate);

}
