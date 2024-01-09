package com.coolpeace.api.domain.member.repository;

import com.coolpeace.api.domain.member.entity.Member;

import java.util.Optional;

public interface MemberRepositoryCustom {
    Optional<Member> findMemberAndRolesByEmail(String email);
}
