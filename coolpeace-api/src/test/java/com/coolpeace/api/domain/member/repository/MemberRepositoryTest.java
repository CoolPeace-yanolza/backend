package com.coolpeace.api.domain.member.repository;

import com.coolpeace.api.global.builder.MemberTestBuilder;
import com.coolpeace.api.global.common.QueryDSLRepositoryTest;
import com.coolpeace.core.domain.member.entity.Member;
import com.coolpeace.core.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

class MemberRepositoryTest extends QueryDSLRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("이메일로 멤버의 정보와 규칙을 조회할 수 있다.")
    @Test
    void findMemberAndRolesByEmail_success() {
        // given
        Member member = new MemberTestBuilder().encoded().build();
        memberRepository.save(member);

        // when
        Optional<Member> resultMember = memberRepository.findMemberAndRolesByEmail(member.getEmail());

        // then
        Assertions.assertTrue(resultMember.isPresent());
        Assertions.assertEquals(member.getEmail(), resultMember.get().getEmail());
        Assertions.assertEquals(member.getName(), resultMember.get().getName());
        Assertions.assertIterableEquals(member.getRoles(), resultMember.get().getRoles());
    }
}