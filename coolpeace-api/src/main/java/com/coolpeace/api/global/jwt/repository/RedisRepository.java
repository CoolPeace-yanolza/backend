package com.coolpeace.api.global.jwt.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Component
public abstract class RedisRepository {
    private final StringRedisTemplate stringRedisTemplate;

    abstract String getPrefix();

    void save(String key, String value, long expire) {
        stringRedisTemplate.opsForValue().set(getPrefix() + key, value, expire, TimeUnit.MILLISECONDS);
    }

    Optional<String> findValueByKey(String key) {
        return Optional.ofNullable(stringRedisTemplate.opsForValue().get(getPrefix() + key));
    }

    Long getExpire(String key) {
        Long expireTime = stringRedisTemplate.getExpire(getPrefix() + key);
        if (expireTime == null) {
            return 0L;
        } else {
            return expireTime * 1000;
        }
    }

    void deleteByKey(String key) {
        stringRedisTemplate.delete(getPrefix() + key);
    }
}
