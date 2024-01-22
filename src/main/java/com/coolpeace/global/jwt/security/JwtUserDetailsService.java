package com.coolpeace.global.jwt.security;

import com.coolpeace.domain.member.entity.Member;
import com.coolpeace.domain.member.service.MemberService;
import com.coolpeace.global.exception.ApplicationException;
import com.coolpeace.global.exception.ErrorCode;
import com.coolpeace.global.jwt.exception.JwtAuthenticationException;
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
            return MemberPrincipal.from(storedMember);
        } catch (ApplicationException e) {
            throw new JwtAuthenticationException(ErrorCode.MEMBER_NOT_FOUND);
        }
    }
}
