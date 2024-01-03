package com.coolpeace.global.jwt.repository;

import com.coolpeace.global.jwt.entity.JwtRefreshTokenRedisEntity;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class JwtRefreshTokenRedisRepository extends RedisRepository {
    private final static String JWT_KEY_PREFIX = "jwt:refresh:";

    public JwtRefreshTokenRedisRepository(StringRedisTemplate stringRedisTemplate) {
        super(stringRedisTemplate);
    }

    @Override
    String getPrefix() {
        return JWT_KEY_PREFIX;
    }

    public void save(JwtRefreshTokenRedisEntity entity) {
        String key = entity.memberEmail();
        String value = entity.refreshToken();
        long expire = entity.expiration();
        super.save(key, value, expire);
    }

    @Override
    public Optional<String> findValueByKey(String key) {
        return super.findValueByKey(key);
    }

    @Override
    public Long getExpire(String key) {
        return super.getExpire(key);
    }

    @Override
    public void deleteByKey(String key) {
        super.deleteByKey(key);
    }
}
