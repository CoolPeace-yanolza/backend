package com.coolpeace.domain.settlement.repository;

import com.coolpeace.domain.accommodation.entity.Accommodation;
import com.coolpeace.domain.member.entity.Member;
import com.coolpeace.domain.settlement.entity.Settlement;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettlementRepository extends JpaRepository<Settlement, Long> {

    List<Settlement> findAllByAccommodation(Accommodation accommodation);
}
