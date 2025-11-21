package com.jian.community.global.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final JsonResponseBodyWriter jsonResponseBodyWriter;

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException {

        if (response.isCommitted()) return;

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        jsonResponseBodyWriter.write(response, new ErrorResponse(
                ErrorCode.INVALID_CREDENTIALS,
                ErrorMessage.INVALID_CREDENTIALS
        ));
    }
}
