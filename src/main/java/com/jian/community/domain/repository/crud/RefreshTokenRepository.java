package com.jian.community.domain.repository.crud;

import java.util.Optional;

public interface RefreshTokenRepository {

    void save(String refreshToken, Long userId, long expiresInMillis);

    Optional<Long> findUserIdByRefreshToken(String refreshToken);

    void delete(String refreshToken);
}
