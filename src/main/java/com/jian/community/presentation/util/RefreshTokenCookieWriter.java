package com.jian.community.presentation.util;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class RefreshTokenCookieWriter {

    @Value("${jwt.refresh-token.cookie-name}")
    private String refreshTokenCookieName;

    @Value("${jwt.refresh-token.validity}")
    private long refreshTokenValidityMs;

    public void writeCookie(String value, HttpServletResponse response) {
        ResponseCookie cookie = createCookie(value);
        response.addHeader("Set-Cookie", cookie.toString());
    }

    private ResponseCookie createCookie(String value) {
        int maxAge = (int) (refreshTokenValidityMs / 1000);

        return ResponseCookie.from(refreshTokenCookieName, value)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .sameSite("None")
                .maxAge(maxAge)
                .build();
    }
}
