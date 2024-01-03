package com.coolpeace.global.jwt.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@RequiredArgsConstructor
public class CustomPrefixKeySerializer extends StringRedisSerializer {
    private final String prefix;
    @Override
    public byte[] serialize(String value) {
        return super.serialize(prefix + value);
    }

    @Override
    public String deserialize(byte[] bytes) {
        String deserialized = super.deserialize(bytes);
        return deserialized == null ? null : deserialized.substring(prefix.length());
    }
}
