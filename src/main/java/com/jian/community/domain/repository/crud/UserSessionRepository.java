package com.jian.community.domain.repository.crud;

import com.jian.community.domain.model.UserSession;

import java.util.List;
import java.util.Optional;

public interface UserSessionRepository {

    UserSession save(UserSession userSession);

    Optional<UserSession> findById(String id);

    List<UserSession> findByUserId(Long userId);

    void deleteById(String id);
}
