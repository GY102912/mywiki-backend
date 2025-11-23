package com.jian.community.global.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.util.AntPathMatcher;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ExcludeUrlPatternMatcher {

    private static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();
    private static final List<ExcludeUrlPattern> EXCLUDE_URL_PATTERNS = List.of(
            new ExcludeUrlPattern(HttpMethod.POST, "/tokens"),
            new ExcludeUrlPattern(HttpMethod.GET, "/swagger-ui/**"),
            new ExcludeUrlPattern(HttpMethod.GET, "/v3/api-docs/**")
    );

    public static boolean matchesAny(String requestHttpMethod, String requestURI) {
        return EXCLUDE_URL_PATTERNS.stream()
                .anyMatch(exclude ->
                        ANT_PATH_MATCHER.match(exclude.urlPattern, requestURI)
                                && exclude.httpMethod.name().equals(requestHttpMethod));
    }

    private record ExcludeUrlPattern(HttpMethod httpMethod, String urlPattern) {};
}
