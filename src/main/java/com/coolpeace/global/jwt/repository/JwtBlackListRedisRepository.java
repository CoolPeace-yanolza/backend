package com.coolpeace.global.jwt.repository;

import com.coolpeace.global.jwt.entity.JwtBlackListRedisEntity;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
public class JwtBlackListRedisRepository implements RedisRepository<JwtBlackListRedisEntity> {
    private final static String JWT_KEY_PREFIX = "jwt:black:";
    private final StringRedisTemplate stringRedisTemplate;

    public JwtBlackListRedisRepository(StringRedisTemplate stringRedisTemplate) {
        stringRedisTemplate.setKeySerializer(new CustomPrefixKeySerializer(JWT_KEY_PREFIX));
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public void save(JwtBlackListRedisEntity entity) {
        String key = entity.accessToken();
        String value = entity.status();
        long expire = entity.expiration();
        stringRedisTemplate.opsForValue().set(key, value, expire, TimeUnit.MILLISECONDS);
    }

    @Override
    public Optional<String> findValueByKey(String key) {
        return Optional.ofNullable(stringRedisTemplate.opsForValue().get(key));
    }

    @Override
    public Long getExpire(String key) {
        Long expireTime = stringRedisTemplate.getExpire(key);
        if (expireTime == null) {
            return -1L;
        } else {
            return expireTime * 1000;
        }
    }

    @Override
    public void deleteByKey(String key) {
        stringRedisTemplate.delete(key);
    }
}
