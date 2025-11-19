package com.jian.community.infrastructure.persistence.jpa;

import com.jian.community.domain.model.User;
import com.jian.community.domain.repository.crud.UserRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserJpaRepository extends UserRepository, JpaRepository<User, Long> {

    @Override
    User save(User user);

    @Override
    Optional<User> findByIdAndIsDeletedFalse(Long userId);

    @Override
    Optional<User> findByEmailAndIsDeletedFalse(String email);

    @Override
    boolean existsByEmailAndIsDeletedFalse(String email);

    @Override
    boolean existsByNicknameAndIsDeletedFalse(String nickname);
}
