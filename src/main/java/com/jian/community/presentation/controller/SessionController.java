package com.jian.community.presentation.controller;

import com.jian.community.application.service.SessionManager;
import com.jian.community.application.service.UserService;
import com.jian.community.presentation.dto.CreateSessionRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Session", description = "세션 기반 인증 관련 API")
@RestController
@RequestMapping("/sessions")
@AllArgsConstructor
public class SessionController {

    private final UserService userService;
    private final SessionManager sessionManager;

    @Operation(summary = "로그인")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400",
                    content = @Content(
                            examples = @ExampleObject(name = "유효하지 않은 인증 정보", value = """
                                            {
                                                "code": "INVALID_CREDENTIALS",
                                                "message": "인증 정보가 올바르지 않습니다."
                                            }
                                            """)))})
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void logIn(@RequestBody CreateSessionRequest request, HttpServletResponse httpResponse) {
        Long userId = userService.authenticate(request.email(), request.password());
        sessionManager.createSession(userId, httpResponse);
    }

    @Operation(summary = "로그아웃")
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logOut(HttpServletRequest httpRequest){
        sessionManager.expireSession(httpRequest);
    }
}
