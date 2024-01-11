package com.coolpeace.domain.accommodation.repository;

import com.coolpeace.domain.accommodation.entity.Accommodation;
import com.coolpeace.domain.member.entity.Member;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccommodationRepository extends JpaRepository<Accommodation, Long>,AccommodationRepositoryCustom{

    List<Accommodation> findAllByMember(Member member);

    List<Accommodation> findAllByMemberId(Long memberId);

    List<Accommodation> findByAddress(String address);
}
