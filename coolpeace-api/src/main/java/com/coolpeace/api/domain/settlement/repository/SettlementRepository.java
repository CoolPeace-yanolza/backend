package com.coolpeace.api.domain.settlement.repository;

import com.coolpeace.api.domain.accommodation.entity.Accommodation;
import com.coolpeace.api.domain.settlement.entity.Settlement;

import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettlementRepository extends JpaRepository<Settlement, Long>,SettlementRepositoryCustom {
    Page<Settlement> findAllByAccommodationAndCouponUseDateAfterAndCouponUseDateBeforeOrderBySumPrice
        (Pageable pageable, Accommodation accommodation, LocalDate startDate, LocalDate endDate);
    Page<Settlement> findAllByAccommodationAndCouponUseDateAfterAndCouponUseDateBeforeOrderByCompleteAt
        (Pageable pageable, Accommodation accommodation, LocalDate startDate, LocalDate endDate);
    Page<Settlement> findAllByAccommodationAndCouponUseDateAfterAndCouponUseDateBeforeOrderByCouponCount
        (Pageable pageable, Accommodation accommodation, LocalDate startDate, LocalDate endDate);
    Page<Settlement> findAllByAccommodationAndCouponUseDateAfterAndCouponUseDateBeforeOrderByCouponUseDate
        (Pageable pageable, Accommodation accommodation, LocalDate startDate, LocalDate endDate);

}
