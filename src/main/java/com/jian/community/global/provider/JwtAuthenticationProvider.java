package com.jian.community.global.provider;

import com.jian.community.application.exception.InvalidCredentialsException;
import com.jian.community.domain.model.Role;
import com.jian.community.domain.repository.crud.AccessTokenBlacklistRepository;
import com.jian.community.domain.repository.crud.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final JwtTokenProvider jwtTokenProvider;
    private final AccessTokenBlacklistRepository accessTokenBlacklistRepository;
    private final UserRepository userRepository;

    @Override
    public Authentication authenticate(Authentication authentication) {
        String token = (String) authentication.getCredentials();
        if (!jwtTokenProvider.validateAccessToken(token) || accessTokenBlacklistRepository.isBlacklisted(token)) {
            throw new InvalidCredentialsException();
        }

        try {
            Claims claims = jwtTokenProvider.parseClaims(token);

            Long userId = Long.parseLong(claims.getSubject());
            Role role = userRepository.findByIdAndIsDeletedFalseOrThrow(userId).getRole();
            List<GrantedAuthority> userRoles = List.of(new SimpleGrantedAuthority(role.name()));

            return new JwtAuthenticationToken(token, userId, userRoles);

        } catch (Exception e) {
            throw new InvalidCredentialsException();
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
