package com.jian.community.global.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jian.community.global.exception.ErrorCode;
import com.jian.community.global.exception.ErrorMessage;
import com.jian.community.global.exception.ErrorResponse;
import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@AllArgsConstructor
public class RateLimitFilter extends OncePerRequestFilter {

    private final Bucket bucket;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain
    ) throws ServletException, IOException {

        if (bucket.tryConsume(1)) {
            filterChain.doFilter(request, response);

        } else {
            handleTooManyRequestException(response);
        }
    }

    private void handleTooManyRequestException(HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        response.setContentType("application/json;charset=UTF-8");

        ErrorResponse error = new ErrorResponse(
                ErrorCode.TOO_MANY_REQUESTS,
                ErrorMessage.TOO_MANY_REQUESTS
        );
        new ObjectMapper().writeValue(response.getWriter(), error);
    }
}
