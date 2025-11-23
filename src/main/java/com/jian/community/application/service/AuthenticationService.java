package com.jian.community.application.service;

import com.jian.community.application.exception.InvalidCredentialsException;
import com.jian.community.application.exception.ResourceNotFoundException;
import com.jian.community.domain.model.User;
import com.jian.community.domain.repository.crud.AccessTokenBlacklistRepository;
import com.jian.community.domain.repository.crud.RefreshTokenRepository;
import com.jian.community.domain.repository.crud.UserRepository;
import com.jian.community.global.exception.ErrorMessage;
import com.jian.community.global.provider.JwtTokenProvider;
import com.jian.community.presentation.dto.TokensResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    @Value("${jwt.refresh-token.validity}")
    private long refreshTokenValidityMs;

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AccessTokenBlacklistRepository accessTokenBlacklistRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public TokensResponse login(String email, String password) {
        User user = userRepository.findByEmailAndIsDeletedFalse(email)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessage.USER_NOT_EXISTS));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        Long userId = user.getId();
        Collection<? extends GrantedAuthority> userRoles = Collections.emptyList();
        Instant issuedAt = Instant.now();

        String accessToken = jwtTokenProvider.generateAccessToken(userId, userRoles, issuedAt);
        String refreshToken = jwtTokenProvider.generateRefreshToken(userId, issuedAt);
        refreshTokenRepository.save(refreshToken, userId, refreshTokenValidityMs);

        return new TokensResponse(accessToken, refreshToken);
    }

    public void logout(String accessToken) {
        if (accessToken == null || !jwtTokenProvider.validateAccessToken(accessToken)) return;

        Instant expiresAt = jwtTokenProvider.parseClaims(accessToken).getExpiration().toInstant();
        long expiresInMillis = Duration.between(expiresAt, Instant.now()).toMillis();

        accessTokenBlacklistRepository.blacklist(accessToken, expiresInMillis);
    }

    public TokensResponse reissue(String refreshToken, String accessToken) {
        if (!StringUtils.hasText(refreshToken)) {
            throw new InvalidCredentialsException();
        }

        Long userId = refreshTokenRepository.findUserIdByRefreshToken(refreshToken)
                .orElseThrow(InvalidCredentialsException::new);
        Collection<? extends GrantedAuthority> userRoles = jwtTokenProvider.parseClaims(refreshToken).get("roles", Collection.class);
        Instant issuedAt = Instant.now();

        String reissuedAccessToken = jwtTokenProvider.generateAccessToken(userId, userRoles, issuedAt);
        String reissuedRefreshToken = jwtTokenProvider.generateRefreshToken(userId, issuedAt);
        refreshTokenRepository.save(reissuedRefreshToken, userId, refreshTokenValidityMs);

        logout(accessToken); // 기존 액세스 토큰 블랙리스트
        refreshTokenRepository.delete(refreshToken); // 기존 리프레시 토큰 삭제
        return new TokensResponse(reissuedAccessToken, reissuedRefreshToken);
    }
}
