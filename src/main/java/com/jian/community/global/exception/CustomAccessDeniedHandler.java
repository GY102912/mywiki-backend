package com.jian.community.global.exception;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final JsonResponseBodyWriter jsonResponseBodyWriter;

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException
    ) throws IOException, ServletException {

        if (response.isCommitted()) return;

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        jsonResponseBodyWriter.write(response, new ErrorResponse(
                ErrorCode.ACCESS_DENIED,
                ErrorMessage.ACCESS_DENIED
        ));
    }
}
