package com.coolpeace.domain.member.dto.response;

import com.coolpeace.global.jwt.dto.JwtPair;

public record MemberRegisterResponse (
        String accessToken,
        String refreshToken,
        long expiresIn
) {
    public static MemberRegisterResponse from(JwtPair jwtPair) {
        return new MemberRegisterResponse(jwtPair.accessToken(), jwtPair.refreshToken(), jwtPair.expiresIn());
    }
}
