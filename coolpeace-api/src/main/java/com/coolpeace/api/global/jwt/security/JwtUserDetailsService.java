package com.coolpeace.api.global.jwt.security;

import com.coolpeace.core.domain.member.entity.Member;
import com.coolpeace.api.global.jwt.exception.JwtAuthenticationException;
import com.coolpeace.api.domain.member.service.MemberService;
import com.coolpeace.core.exception.ApplicationException;
import com.coolpeace.core.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class JwtUserDetailsService implements UserDetailsService {

    private final MemberService memberService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            Member storedMember = memberService.getMemberByEmail(email);
            return JwtPrincipal.from(storedMember);
        } catch (ApplicationException e) {
            throw new JwtAuthenticationException(ErrorCode.MEMBER_NOT_FOUND);
        }
    }
}
