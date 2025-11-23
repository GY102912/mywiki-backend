package com.jian.community.global.provider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Collection;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final String AUTHORIZATION_HEADER = "Authorization";
    private final String TOKEN_PREFIX = "Bearer ";

    @Value("${jwt.access-token.cookie-name}")
    private String accessTokenCookieName;

    @Value("${jwt.access-token.validity}")
    private long accessTokenValidityMs;

    @Value("${jwt.refresh-token.validity}")
    private long refreshTokenValidityMs;

    private final SecretKey key;

    public String resolveAccessToken(HttpServletRequest request) {
        String bearer = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearer) && bearer.startsWith(TOKEN_PREFIX)) {
            return bearer.substring(TOKEN_PREFIX.length());
        }
        return null;
    }

    public String resolveRefreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;

        for (Cookie cookie : cookies) {
            if (StringUtils.hasText(cookie.getName()) && accessTokenCookieName.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    public Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateAccessToken(String token) {
        Instant expiration = parseClaims(token).getExpiration().toInstant();
        return expiration.isAfter(Instant.now());
    }

    public String generateAccessToken(Long userId, Collection<? extends GrantedAuthority> roles, Instant issuedAt) {
        Instant expiresAt = issuedAt.plusSeconds(accessTokenValidityMs);
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("roles", roles)
                .setIssuedAt(Date.from(issuedAt))
                .setExpiration(Date.from(expiresAt))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(Long userId, Instant issuedAt) {
        Instant expiresAt = issuedAt.plusSeconds(refreshTokenValidityMs);
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setIssuedAt(Date.from(issuedAt))
                .setExpiration(Date.from(expiresAt))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}
