package com.jian.community.infrastructure.persistence.memory;

import com.jian.community.domain.model.UserSession;
import com.jian.community.domain.repository.crud.UserSessionRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@Primary
public class UserSessionInMemoryRepository implements UserSessionRepository {

    private final Map<String, UserSession> store = new ConcurrentHashMap<>();
    private final Map<Long, List<UserSession>> userIdIndex = new ConcurrentHashMap<>();

    @Override
    public UserSession save(UserSession userSession) {
        store.put(userSession.getId(), userSession);
        userIdIndex.compute(userSession.getUserId(), (k, v) -> {
            if (v == null) v = new ArrayList<>(); // 초기화
            else v.removeIf(oldSession ->
                    oldSession.getId().equals(userSession.getId())); // 아이디가 같은 기존 세션 삭제
            v.add(userSession); // 새로운 세션 추가
            return v;
        });
        return userSession;
    }

    @Override
    public Optional<UserSession> findById(String id) {
        UserSession userSession = store.get(id);
        return Optional.ofNullable(userSession);
    }

    @Override
    public List<UserSession> findByUserId(Long userId) {
        List<UserSession> results = userIdIndex.get(userId);
        return List.copyOf(results);
    }

    @Override
    public void deleteById(String id) {
        UserSession deleted = store.remove(id);
        if (deleted == null) return; // 아무 것도 삭제하지 않은 경우

        List<UserSession> sessions = userIdIndex.get(deleted.getUserId());
        if (sessions == null) return; // 인덱스가 존재하지 않는 경우

        sessions.removeIf(session -> session.getId().equals(id));
        if (sessions.isEmpty()) userIdIndex.remove(deleted.getUserId());
    }
}
