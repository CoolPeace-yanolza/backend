package com.coolpeace.global.jwt.repository;

import com.coolpeace.global.jwt.entity.JwtRefreshTokenRedisEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class JwtRefreshTokenRedisRepositoryTest {

    @Autowired
    private JwtRefreshTokenRedisRepository repository;

    @Test
    void shouldSuccessToSaveValue() {
        // given
        String key = "test@test.com";
        String value = "fake-refresh-token";
        long expiration = 3600000L;
        JwtRefreshTokenRedisEntity entity = new JwtRefreshTokenRedisEntity(key, value, expiration);

        // when
        repository.save(entity);

        // then
        assertEquals(value,repository.findValueByKey(entity.memberEmail()).orElse(null));
    }
}