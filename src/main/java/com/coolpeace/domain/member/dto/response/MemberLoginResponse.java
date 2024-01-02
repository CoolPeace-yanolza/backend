package com.coolpeace.domain.member.dto.response;

import com.coolpeace.global.jwt.dto.JwtPair;

public record MemberLoginResponse (
        String accessToken,
        String refreshToken,
        long expiresIn
) {
    public static MemberLoginResponse from(JwtPair jwtPair) {
        return new MemberLoginResponse(jwtPair.accessToken(), jwtPair.refreshToken(), jwtPair.expiresIn());
    }
}
