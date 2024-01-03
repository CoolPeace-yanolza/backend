package com.coolpeace.global.jwt.entity;

import jakarta.persistence.Id;
import org.springframework.data.redis.core.TimeToLive;

import java.util.concurrent.TimeUnit;

public record JwtRefreshTokenRedisEntity(
        @Id
        String memberEmail,
        String refreshToken,
        @TimeToLive(unit = TimeUnit.MILLISECONDS)
        long expiration
) {
}
