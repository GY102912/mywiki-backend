package com.jian.community.application.service;

import com.jian.community.application.exception.InvalidCredentialsException;
import com.jian.community.application.exception.ResourceNotFoundException;
import com.jian.community.domain.model.User;
import com.jian.community.domain.repository.crud.UserRepository;
import com.jian.community.global.exception.ErrorMessage;
import com.jian.community.global.provider.JwtTokenProvider;
import com.jian.community.presentation.dto.TokensResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public TokensResponse authenticate(String email, String password) {
        User user = userRepository.findByEmailAndIsDeletedFalse(email)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessage.USER_NOT_EXISTS));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        Long userId = user.getId();
        List<String> userRoles = List.of();
        Instant issuedAt = Instant.now();

        String accessToken = jwtTokenProvider.generateAccessToken(userId, userRoles, issuedAt);
        String refreshToken = jwtTokenProvider.generateRefreshToken(userId, issuedAt);

        return new TokensResponse(accessToken, refreshToken);
    }
}
