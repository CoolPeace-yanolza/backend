package com.coolpeace.core.domain.member.repository;

import com.coolpeace.core.domain.member.entity.Member;

import java.util.Optional;

public interface MemberRepositoryCustom {
    Optional<Member> findMemberAndRolesByEmail(String email);
}
