package com.jian.community.global.provider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${jwt.access-token-validity}")
    private long accessTokenValidityMs;

    @Value("${jwt.refresh-token-validity}")
    private long refreshTokenValidityMs;

    private final SecretKey key;

    public String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearer) && bearer.startsWith(TOKEN_PREFIX)) {
            return bearer.substring(TOKEN_PREFIX.length());
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

    public String generateAccessToken(Long userId, Collection<String> roles, Instant issuedAt) {
        Instant expiration = issuedAt.plusSeconds(accessTokenValidityMs);
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("roles", roles)
                .setIssuedAt(Date.from(issuedAt))
                .setExpiration(Date.from(expiration))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(Long userId, Instant issuedAt) {
        Instant expiration = issuedAt.plusSeconds(refreshTokenValidityMs);
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setIssuedAt(Date.from(issuedAt))
                .setExpiration(Date.from(expiration))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}
