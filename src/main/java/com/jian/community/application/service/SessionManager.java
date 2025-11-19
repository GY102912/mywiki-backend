package com.jian.community.application.service;

import com.jian.community.domain.event.UserDeleteEvent;
import com.jian.community.domain.exception.SessionExpiredException;
import com.jian.community.domain.model.UserSession;
import com.jian.community.domain.repository.crud.UserSessionRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class SessionManager {

    private static final String SESSION_COOKIE_NAME = "JSESSIONID";
    private static final Duration SESSION_TTL = Duration.ofHours(2);

    private final UserSessionRepository userSessionRepository;

    @Transactional
    public void createSession(Long userId, HttpServletResponse httpResponse) {
        String sessionId = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now();
        UserSession session = UserSession.of(
                sessionId,
                userId,
                now,
                now.plus(SESSION_TTL)
        );
        userSessionRepository.save(session);

        Cookie cookie = new Cookie(SESSION_COOKIE_NAME, sessionId);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        httpResponse.addCookie(cookie);
    }

    @Transactional
    public UserSession getValidSession(HttpServletRequest httpRequest) {
        UserSession session = getSession(httpRequest);

        if (session.isExpired()) {
            expireSession(session.getId());
            throw new SessionExpiredException();
        }

        return extendSession(session);
    }

    @Transactional(readOnly = true)
    public UserSession getSession(HttpServletRequest httpRequest) {
        Optional<UserSession> session = getSessionId(httpRequest)
                .flatMap(userSessionRepository::findById);

        return session.orElseThrow(SessionExpiredException::new);
    }

    @Transactional
    public void expireSession(HttpServletRequest httpRequest) {
        getSessionId(httpRequest)
                .ifPresent(this::expireSession);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void expireSession(UserDeleteEvent event) {
        userSessionRepository.findByUserId(event.userId())
                .forEach(session -> expireSession(session.getId()));
    }

    private void expireSession(String sessionId) {
        userSessionRepository.deleteById(sessionId);
    }

    private UserSession extendSession(UserSession session) {
        LocalDateTime now = LocalDateTime.now();
        session.extendSession(now, SESSION_TTL);
        return userSessionRepository.save(session);
    }

    private Optional<String> getSessionId(HttpServletRequest httpRequest) {
        return Optional.ofNullable(httpRequest.getCookies()).stream()
                .flatMap(Arrays::stream)
                .filter(cookie -> SESSION_COOKIE_NAME.equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue);
    }
}
