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
import com.coolpeace.global.jwt.dto.JwtPayload;
import com.coolpeace.global.jwt.service.JwtService;
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
    private final JwtService jwtService;

    @Transactional(readOnly = true)
    public Member getMemberByEmail(String email) {
        return memberRepository.findMemberAndRolesByEmail(email).orElseThrow(MemberNotFoundException::new);
    }

    @Transactional
    public JwtPair login(MemberLoginRequest loginRequest) {
        Member storedMember = memberRepository.findByEmail(loginRequest.email())
                .orElseThrow(MemberNotFoundException::new);

        if (!passwordEncoder.matches(loginRequest.password(), storedMember.getPassword())) {
            throw new MemberWrongPasswordException();
        }

        return jwtService.createTokenPair(
                JwtPayload.fromNow(String.valueOf(storedMember.getId()), storedMember.getEmail()));
    }

    @Transactional
    public JwtPair registerAsOwner(MemberRegisterRequest registerRequest) {
        validateMemberEmail(registerRequest.email());
        Role role = roleRepository.findByRoleType(RoleType.OWNER).orElseThrow(MemberRoleNotFoundException::new);
        Member newMember = memberRepository.save(
                Member.of(registerRequest.email(), passwordEncoder.encode(registerRequest.password()), role)
        );

        return jwtService.createTokenPair(
                JwtPayload.fromNow(String.valueOf(newMember.getId()), newMember.getEmail()));
    }

    @Transactional(readOnly = true)
    public void emailCheck(MemberRegisterEmailCheckRequest emailCheckRequest) {
        validateMemberEmail(emailCheckRequest.email());
    }

    @Transactional
    public void logout(String email, String accessToken) {
        jwtService.deleteRefreshToken(email);
    }

    @Transactional
    public JwtPair refreshAccessToken(String refreshToken) {
        JwtPayload jwtPayload = jwtService.verifyRefreshToken(refreshToken);

        // TODO: 저장된 리프레시토큰 확인

        JwtPayload newJwtPayload = JwtPayload.fromNow(jwtPayload.id(), jwtPayload.email());
        return jwtService.createTokenPair(jwtService.createAccessToken(newJwtPayload), refreshToken);
    }

    private void validateMemberEmail(String registerRequest) {
        if (memberRepository.existsByEmail(registerRequest)) {
            throw new MemberAlreadyExistedException();
        }
    }
}
