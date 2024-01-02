package com.coolpeace.global.jwt.dto;

public record JwtPair (
        String accessToken,
        String refreshToken,
        long expiresIn
) {
    public static JwtPair from(String accessToken, String refreshToken, long expiresIn) {
        return new JwtPair(accessToken, refreshToken, expiresIn / 1000);
    }
}