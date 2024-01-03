package com.coolpeace.global.jwt.repository;

import java.util.Optional;

public interface RedisRepository<T> {
    void save(T entity);
    Optional<String> findValueByKey(String key);
    Long getExpire(String key);
    void deleteByKey(String key);
}
