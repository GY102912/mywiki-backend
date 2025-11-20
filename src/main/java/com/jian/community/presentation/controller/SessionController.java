package com.jian.community.presentation.controller;

import com.jian.community.application.service.AuthenticationService;
import com.jian.community.application.service.SessionManager;
import com.jian.community.application.service.UserService;
import com.jian.community.presentation.dto.CreateSessionRequest;
import com.jian.community.presentation.dto.LoginResponse;
import com.jian.community.presentation.dto.TokensResponse;
import com.jian.community.presentation.util.RefreshTokenCookieWriter;
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
@RequestMapping("/tokens")
@AllArgsConstructor
public class SessionController {

    private final AuthenticationService authenticationService;
    private final SessionManager sessionManager;
    private final RefreshTokenCookieWriter refreshTokenCookieWriter;

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
    public LoginResponse logIn(@RequestBody CreateSessionRequest request, HttpServletResponse httpResponse) {
        TokensResponse tokens = authenticationService.authenticate(request.email(), request.password());
        refreshTokenCookieWriter.writeCookie(tokens.refreshToken(), httpResponse);
        return new LoginResponse(tokens.accessToken());
    }

    @Operation(summary = "로그아웃")
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logOut(HttpServletRequest httpRequest){
        sessionManager.expireSession(httpRequest);
    }
}
