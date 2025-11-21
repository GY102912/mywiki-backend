package com.jian.community.infrastructure.redis;

import com.jian.community.domain.repository.crud.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRedisRepository implements RefreshTokenRepository {

    private final RedisTemplate<String, String> redisTemplate;
    private final RedisKeyManager redisKeyManager;

    @Override
    public void save(String refreshToken, Long userId, long expiresInMillis) {
        String key = redisKeyManager.getRefreshTokenKey(refreshToken);
        String value = String.valueOf(userId);
        redisTemplate.opsForValue().set(key, value, expiresInMillis, TimeUnit.MILLISECONDS);
    }

    @Override
    public Optional<Long> findUserIdByRefreshToken(String refreshToken) {
        String key = redisKeyManager.getRefreshTokenKey(refreshToken);
        String value = redisTemplate.opsForValue().get(key);
        return Optional.ofNullable(value).map(Long::valueOf);
    }

    @Override
    public void delete(String refreshToken) {
        String key = redisKeyManager.getRefreshTokenKey(refreshToken);
        redisTemplate.delete(key);
    }
}
