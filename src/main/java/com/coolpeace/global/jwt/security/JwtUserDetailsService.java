package com.coolpeace.global.jwt.security;

import com.coolpeace.domain.member.entity.Member;
import com.coolpeace.domain.member.service.MemberService;
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
        Member storedMember = memberService.getMemberByEmail(email);
        return JwtPrincipal.from(storedMember);
    }
}
