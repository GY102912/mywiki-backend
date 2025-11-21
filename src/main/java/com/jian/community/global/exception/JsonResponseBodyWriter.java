package com.jian.community.global.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JsonResponseBodyWriter {

    private final ObjectMapper objectMapper;

    public void write(HttpServletResponse response, Object body) throws IOException {
        String json = objectMapper.writeValueAsString(body);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(json);
    }
}
