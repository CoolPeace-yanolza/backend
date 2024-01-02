package com.coolpeace.domain.member.service;

import com.coolpeace.global.jwt.dto.JwtPair;
import com.coolpeace.domain.member.dto.request.MemberLoginRequest;
import com.coolpeace.domain.member.dto.request.MemberRegisterEmailCheckRequest;
import com.coolpeace.domain.member.dto.request.MemberRegisterRequest;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    public JwtPair login(MemberLoginRequest loginRequest) {
        return null;
    }

    public JwtPair register(MemberRegisterRequest registerRequest) {
        return null;
    }

    public void emailCheck(MemberRegisterEmailCheckRequest emailCheckRequest) {

    }

    public void logout(String email, String accessToken) {

    }

    public JwtPair refreshAccessToken(String refreshToken) {
        return null;
    }
}
