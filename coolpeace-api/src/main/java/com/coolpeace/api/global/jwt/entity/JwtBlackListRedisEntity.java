package com.coolpeace.api.global.jwt.entity;

import jakarta.persistence.Id;
import org.springframework.data.redis.core.TimeToLive;

import java.util.concurrent.TimeUnit;

public record JwtBlackListRedisEntity(
        @Id
        String accessToken,
        String status,
        @TimeToLive(unit = TimeUnit.MILLISECONDS)
        long expiration
) {
        public static JwtBlackListRedisEntity from(String accessToken, long expiration) {
                return new JwtBlackListRedisEntity(accessToken, "black", expiration);
        }
}
