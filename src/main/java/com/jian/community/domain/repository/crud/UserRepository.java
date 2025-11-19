package com.jian.community.domain.repository.crud;

import com.jian.community.global.exception.ErrorMessage;
import com.jian.community.application.exception.ResourceNotFoundException;
import com.jian.community.domain.model.User;

import java.util.Optional;

public interface UserRepository {

    User save(User user);

    Optional<User> findByIdAndIsDeletedFalse(Long userId);

    Optional<User> findByEmailAndIsDeletedFalse(String email);

    boolean existsByEmailAndIsDeletedFalse(String email);

    boolean existsByNicknameAndIsDeletedFalse(String nickname);

    default User findByIdAndIsDeletedFalseOrThrow(Long userId) {
        return findByIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessage.USER_NOT_EXISTS));
    }
}
