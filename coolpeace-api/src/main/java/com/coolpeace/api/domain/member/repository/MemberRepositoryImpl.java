package com.coolpeace.api.domain.member.repository;

import com.coolpeace.api.domain.member.entity.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.coolpeace.api.domain.member.entity.QMember.member;
import static com.coolpeace.api.domain.member.entity.QMemberRole.memberRole;
import static com.coolpeace.api.domain.member.entity.QRole.role;

@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<Member> findMemberAndRolesByEmail(String email) {
        Member storedMember = jpaQueryFactory.selectFrom(member)
                .innerJoin(member.roles, memberRole).fetchJoin()
                .innerJoin(memberRole.role, role).fetchJoin()
                .where(member.email.eq(email))
                .fetchFirst();
        return Optional.ofNullable(storedMember);
    }
}
