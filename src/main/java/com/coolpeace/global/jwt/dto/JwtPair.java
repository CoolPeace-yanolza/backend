package com.coolpeace.global.jwt.dto;

public record JwtPair (
        String accessToken,
        String refreshToken,
        long expiresIn
) {
}
