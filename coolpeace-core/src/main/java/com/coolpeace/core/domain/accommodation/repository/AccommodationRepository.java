package com.coolpeace.core.domain.accommodation.repository;

import com.coolpeace.core.domain.accommodation.entity.Accommodation;
import com.coolpeace.core.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccommodationRepository extends JpaRepository<Accommodation, Long>,AccommodationRepositoryCustom{

    List<Accommodation> findAllByMember(Member member);

    List<Accommodation> findAllByMemberId(Long memberId);

    List<Accommodation> findByAddress(String address);
}
