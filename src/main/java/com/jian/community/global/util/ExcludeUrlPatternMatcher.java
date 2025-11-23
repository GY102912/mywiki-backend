package com.jian.community.global.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.AntPathMatcher;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ExcludeUrlPatternMatcher {

    private static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();
    private static final List<String> EXCLUDE_URL_PATTERNS = List.of(
            "/tokens",
            "/swagger-ui/**",
            "/v3/api-docs/**"
    );

    public static boolean matchesAny(String requestURI) {
        return EXCLUDE_URL_PATTERNS.stream()
                .anyMatch(pattern -> ANT_PATH_MATCHER.match(pattern, requestURI));
    }
}
