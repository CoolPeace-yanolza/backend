package com.coolpeace.domain.member.repository;

import com.coolpeace.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);

    @Query("SELECT DISTINCT m FROM Member m JOIN FETCH m.roles r JOIN FETCH r.role WHERE m.email = :email")
    Optional<Member> findMemberAndRolesByEmail(@Param("email") String email);

    boolean existsByEmail(String email);
}
