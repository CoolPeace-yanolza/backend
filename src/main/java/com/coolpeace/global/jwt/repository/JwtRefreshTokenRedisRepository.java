package com.coolpeace.global.jwt.repository;

import com.coolpeace.global.jwt.entity.JwtRefreshTokenRedisEntity;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
public class JwtRefreshTokenRedisRepository implements RedisRepository<JwtRefreshTokenRedisEntity> {
    private final static String JWT_KEY_PREFIX = "jwt:refresh:";
    private final StringRedisTemplate stringRedisTemplate;

    public JwtRefreshTokenRedisRepository(StringRedisTemplate stringRedisTemplate) {
        stringRedisTemplate.setKeySerializer(new CustomPrefixKeySerializer(JWT_KEY_PREFIX));
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public void save(JwtRefreshTokenRedisEntity entity) {
        String key = entity.memberEmail();
        String value = entity.refreshToken();
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
