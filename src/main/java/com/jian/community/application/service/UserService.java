package com.jian.community.application.service;

import com.jian.community.application.util.PasswordEncoder;
import com.jian.community.domain.event.UserDeleteEvent;
import com.jian.community.global.exception.ErrorMessage;
import com.jian.community.application.exception.InvalidCredentialsException;
import com.jian.community.application.exception.UserAlreadyExistsException;
import com.jian.community.domain.model.User;
import com.jian.community.domain.repository.crud.UserRepository;
import com.jian.community.presentation.dto.*;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final ApplicationEventPublisher eventPublisher;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public Long authenticate(String email, String password) {
        Optional<User> user = userRepository.findByEmailAndIsDeletedFalse(email);

        if (user.isEmpty() || !passwordEncoder.matches(password, user.get().getPassword())) {
            throw new InvalidCredentialsException();
        }

        return user.get().getId();
    }

    @Transactional
    public void createUser(CreateUserRequest request) {
        if (userRepository.existsByEmailAndIsDeletedFalse((request.email()))) {
            throw new UserAlreadyExistsException(ErrorMessage.EMAIL_ALREADY_EXISTS);
        }

        if (userRepository.existsByNicknameAndIsDeletedFalse(request.nickname())) {
            throw new UserAlreadyExistsException(ErrorMessage.NICKNAME_ALREADY_EXISTS);
        }

        String encodedPassword = passwordEncoder.encode(request.password());
        User user = User.of(
                request.email(),
                encodedPassword,
                request.nickname(),
                request.profileImageUrl()
        );
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long userId) {
        userRepository.findByIdAndIsDeletedFalse(userId)
                .ifPresent(User::delete);

        eventPublisher.publishEvent(new UserDeleteEvent(userId));
    }

    @Transactional(readOnly = true)
    public UserInfoResponse getUserInfo(Long userId) {
        User user = userRepository.findByIdAndIsDeletedFalseOrThrow(userId);
        return new UserInfoResponse(user.getEmail(), user.getNickname(), user.getProfileImageUrl());
    }

    @Transactional
    public UserInfoResponse updateUserInfo(Long userId, UpdateUserRequest request) {
        User user = userRepository.findByIdAndIsDeletedFalseOrThrow(userId);
        user.update(request.nickname(), request.profileImageUrl());

        return new UserInfoResponse(user.getEmail(), user.getNickname(), user.getProfileImageUrl());
    }

    @Transactional
    public void changePassword(Long userId, ChangePasswordRequest request) {
        User user = userRepository.findByIdAndIsDeletedFalseOrThrow(userId);

        if (!passwordEncoder.matches(request.oldPassword(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        String encodedPassword = passwordEncoder.encode(request.newPassword());
        user.changePassword(encodedPassword);
    }

    @Transactional(readOnly = true)
    public AvailabilityResponse validateEmail(String email) {
        boolean isAvailable = !userRepository.existsByEmailAndIsDeletedFalse(email);
        return new AvailabilityResponse(isAvailable);
    }

    @Transactional(readOnly = true)
    public AvailabilityResponse validateNickname(String nickname) {
        boolean isAvailable = !userRepository.existsByNicknameAndIsDeletedFalse(nickname);
        return new AvailabilityResponse(isAvailable);
    }
}
