package com.coolpeace.domain.member.repository;

import com.coolpeace.domain.member.entity.Member;

import java.util.Optional;

public interface MemberRepositoryCustom {
    Optional<Member> findMemberAndRolesByEmail(String email);
}
