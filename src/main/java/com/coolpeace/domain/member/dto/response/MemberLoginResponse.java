package com.coolpeace.domain.member.dto.response;

import com.coolpeace.global.jwt.dto.JwtPair;

public record MemberLoginResponse (
        String accessToken,
        String refreshToken,
        String name,
        String email,
        long expiresIn
) {
    public static MemberLoginResponse from(String name, String email, JwtPair jwtPair) {
        return new MemberLoginResponse(
                jwtPair.accessToken(),
                jwtPair.refreshToken(),
                name,
                email,
                jwtPair.expiresIn());
    }
}
