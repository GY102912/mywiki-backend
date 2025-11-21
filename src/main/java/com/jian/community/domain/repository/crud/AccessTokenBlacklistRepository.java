package com.jian.community.domain.repository.crud;

public interface AccessTokenBlacklistRepository {

    void blacklist(String accessToken, long expireTimeMs);

    boolean isBlacklisted(String accessToken);
}
