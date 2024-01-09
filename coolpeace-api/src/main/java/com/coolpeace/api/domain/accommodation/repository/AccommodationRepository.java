package com.coolpeace.api.domain.accommodation.repository;

import com.coolpeace.api.domain.accommodation.entity.Accommodation;
import com.coolpeace.api.domain.member.entity.Member;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccommodationRepository extends JpaRepository<Accommodation, Long> {

    List<Accommodation> findAllByMember(Member member);

    List<Accommodation> findAllByMemberId(Long memberId);

    List<Accommodation> findByAddress(String address);
}
