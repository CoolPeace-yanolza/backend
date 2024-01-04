package com.coolpeace.domain.member.dto.response;

import com.coolpeace.global.jwt.dto.JwtPair;

public record MemberLoginResponse (
        String accessToken,
        String refreshToken,
        String name,
        long expiresIn
) {
    public static MemberLoginResponse from(String name, JwtPair jwtPair) {
        return new MemberLoginResponse(jwtPair.accessToken(), jwtPair.refreshToken(), name, jwtPair.expiresIn());
    }
}
