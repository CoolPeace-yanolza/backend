package com.coolpeace.domain.member.service;

import com.coolpeace.domain.member.dto.request.MemberRegisterRequest;
import com.coolpeace.domain.member.entity.Member;
import com.coolpeace.domain.member.entity.Role;
import com.coolpeace.domain.member.entity.type.RoleType;
import com.coolpeace.domain.member.repository.MemberRepository;
import com.coolpeace.domain.member.repository.RoleRepository;
import com.coolpeace.global.builder.MemberTestBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {
    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @DisplayName("멤버 회원가입")
    @Nested
    class MemberRegisterTest {

        @DisplayName("멤버는 숙박업주로서의 회원가입 서비스를 이용할 수 있다.")
        @Test
        void memberRegister_success() {
            // given
            Member member = new MemberTestBuilder().encoded().build();
            MemberRegisterRequest request = new MemberRegisterRequest(
                    member.getEmail(),
                    member.getPassword(),
                    member.getName()
            );

            given(memberRepository.existsByEmail(anyString())).willReturn(false);
            given(roleRepository.findByRoleType(any(RoleType.class)))
                    .willReturn(Optional.of(Role.of(RoleType.OWNER)));
            given(memberRepository.save(any(Member.class)))
                    .willReturn(member);
            given(passwordEncoder.encode(anyString())).willReturn(member.getPassword());

            // when
            memberService.registerAsOwner(request);

            // then
            verify(memberRepository).existsByEmail(anyString());
            verify(roleRepository).findByRoleType(any(RoleType.class));
            verify(memberRepository).save(any(Member.class));
            verify(passwordEncoder).encode(anyString());
        }

    }


}