package com.jian.community.global.provider;

import com.jian.community.application.exception.InvalidCredentialsException;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String token = (String) authentication.getCredentials();

        try {
            Claims claims = jwtTokenProvider.parseClaims(token);
            // TODO: 토큰 유효성 검사

            Long userId = Long.parseLong(claims.getSubject());
            CustomUserDetails userDetails = customUserDetailsService.loadUserByUserId(userId);
            Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

            return new JwtAuthenticationToken(userDetails, authorities);

        } catch (Exception e) {
            throw new InvalidCredentialsException(); // TODO: 세세한 에러 처리
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
