package com.coolpeace.api.domain.member.dto.response;

import com.coolpeace.api.global.jwt.dto.JwtPair;

public record MemberRefreshAccessTokenResponse(
        String accessToken,
        String refreshToken,
        long expiresIn
) {
    public static MemberRefreshAccessTokenResponse from(JwtPair jwtPair) {
        return new MemberRefreshAccessTokenResponse(jwtPair.accessToken(), jwtPair.refreshToken(), jwtPair.expiresIn());
    }
}
