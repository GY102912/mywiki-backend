package com.jian.community.infrastructure.redis;

import com.jian.community.domain.repository.crud.AccessTokenBlacklistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class AccessTokenBlacklistRedisRepository implements AccessTokenBlacklistRepository {

    private final RedisTemplate<String, String> redisTemplate;
    private final RedisKeyManager redisKeyManager;

    @Override
    public void blacklist(String accessToken, long expiresInMillis) {
        String key = redisKeyManager.getBlacklistKey(accessToken);
        redisTemplate.opsForValue().set(key, "", expiresInMillis, TimeUnit.MILLISECONDS);
    }

    @Override
    public boolean isBlacklisted(String accessToken) {
        String key = redisKeyManager.getBlacklistKey(accessToken);
        return Optional.ofNullable(redisTemplate.opsForValue().get(key)).isPresent();
    }
}
