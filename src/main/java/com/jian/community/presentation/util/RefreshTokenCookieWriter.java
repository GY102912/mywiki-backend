package com.jian.community.presentation.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RefreshTokenCookieWriter {

    @Value("${jwt.refresh-token.validity}")
    private long refreshTokenValidityMs;

    public void writeCookie(String value, HttpServletResponse response) {
        Cookie cookie = createCookie(value);
        response.addCookie(cookie);
    }

    private Cookie createCookie(String value) {
        int maxAge = (int) (refreshTokenValidityMs / 1000);

        Cookie cookie = new Cookie("refresh_token", value);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/token");
        cookie.setMaxAge(maxAge);

        return cookie;
    }
}
