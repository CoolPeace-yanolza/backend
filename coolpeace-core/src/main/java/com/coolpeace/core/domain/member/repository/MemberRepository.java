package com.coolpeace.core.domain.member.repository;

import com.coolpeace.core.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {
    Optional<Member> findByEmail(String email);

    boolean existsByEmail(String email);
}
