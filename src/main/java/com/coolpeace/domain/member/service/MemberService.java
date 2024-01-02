package com.coolpeace.domain.member.service;

import com.coolpeace.domain.member.dto.request.MemberLoginRequest;
import com.coolpeace.domain.member.dto.request.MemberRegisterEmailCheckRequest;
import com.coolpeace.domain.member.dto.request.MemberRegisterRequest;
import com.coolpeace.domain.member.entity.Member;
import com.coolpeace.domain.member.entity.Role;
import com.coolpeace.domain.member.entity.type.RoleType;
import com.coolpeace.domain.member.exception.MemberAlreadyExistedException;
import com.coolpeace.domain.member.exception.MemberNotFoundException;
import com.coolpeace.domain.member.exception.MemberRoleNotFoundException;
import com.coolpeace.domain.member.exception.MemberWrongPasswordException;
import com.coolpeace.domain.member.repository.MemberRepository;
import com.coolpeace.domain.member.repository.RoleRepository;
import com.coolpeace.global.jwt.dto.JwtPair;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public JwtPair login(MemberLoginRequest loginRequest) {
        Member storedMember = memberRepository.findByEmail(loginRequest.email())
                .orElseThrow(MemberNotFoundException::new);

        if (!passwordEncoder.matches(loginRequest.password(), storedMember.getPassword())) {
            throw new MemberWrongPasswordException();
        }

        return null;
    }

    @Transactional
    public JwtPair registerAsOwner(MemberRegisterRequest registerRequest) {
        validateMemberEmail(registerRequest.email());
        Role role = roleRepository.findByRoleType(RoleType.OWNER).orElseThrow(MemberRoleNotFoundException::new);
        Member newMember = memberRepository.save(
                Member.of(registerRequest.email(), passwordEncoder.encode(registerRequest.password()), role)
        );

        return null;
    }

    @Transactional(readOnly = true)
    public void emailCheck(MemberRegisterEmailCheckRequest emailCheckRequest) {
        validateMemberEmail(emailCheckRequest.email());
    }

    @Transactional
    public void logout(String email, String accessToken) {

    }

    @Transactional
    public JwtPair refreshAccessToken(String refreshToken) {
        return null;
    }

    private void validateMemberEmail(String registerRequest) {
        if (memberRepository.existsByEmail(registerRequest)) {
            throw new MemberAlreadyExistedException();
        }
    }
}
