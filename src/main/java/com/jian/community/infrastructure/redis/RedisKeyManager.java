package com.jian.community.infrastructure.redis;

import org.springframework.stereotype.Component;

@Component
public class RedisKeyManager {

    private final String REFRESH_PREFIX = "refresh:";
    private final String BLACKLIST_PREFIX = "blacklist:";

    public String getRefreshTokenKey(String key) {
        return REFRESH_PREFIX + key;
    }

    public String getBlacklistKey(String key) {
        return BLACKLIST_PREFIX + key;
    }
}
