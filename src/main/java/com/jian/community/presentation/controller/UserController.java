package com.jian.community.presentation.controller;

import com.jian.community.application.service.UserService;
import com.jian.community.presentation.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User", description = "사용자 관련 API")
@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private UserService userService;

    @Operation(summary = "회원 가입")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400",
                    content = @Content(
                            examples = {
                                    @ExampleObject(name = "유효하지 않은 이메일 형식", value = """
                                            {
                                                "code": "INVALID_USER_INPUT",
                                                "message": "올바르지 않은 이메일 형식입니다.",
                                                "field": "email"
                                            }
                                            """),
                                    @ExampleObject(name = "유효하지 않은 비밀번호 형식", value = """
                                            {
                                                "code": "INVALID_USER_INPUT",
                                                "message": "비밀번호는 8자 이상, 영문과 숫자를 포함해야 합니다.",
                                                "field": "newPassword"
                                            }
                                            """),
                                    @ExampleObject(name = "유효하지 않은 닉네임 형식", value = """
                                            {
                                                "code": "INVALID_USER_INPUT",
                                                "message": "닉네임은 2~20자의 한글, 영문, 숫자만 사용할 수 있습니다.",
                                                "field": "nickname"
                                            }
                                            """)
                            })),
            @ApiResponse(responseCode = "409",
                    content = @Content(
                            examples = {
                                    @ExampleObject(name = "이메일 중복", value = """
                                            {
                                                "code": "USER_ALREADY_EXISTS",
                                                "message": "이미 사용 중인 이메일입니다."
                                            }
                                            """),
                                    @ExampleObject(name = "닉네임 중복", value = """
                                            {
                                                "code": "USER_ALREADY_EXISTS",
                                                "message": "이미 사용 중인 닉네임입니다."
                                            }
                                            """)
                                    }))})
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void signUp(@Valid @RequestBody CreateUserRequest request) {
        userService.createUser(request);
    }

    @Operation(summary = "내 정보 조회", description = "로그인한 사용자의 정보를 조회합니다.")
    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    public UserInfoResponse getMyInfo(@RequestAttribute Long userId) {
        return userService.getUserInfo(userId);
    }

    @Operation(summary = "내 정보 수정", description = "로그인한 사용자의 닉네임을 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400",
                    content = @Content(
                            examples = @ExampleObject(name = "유효하지 않은 닉네임 형식", value = """
                                            {
                                                "code": "INVALID_USER_INPUT",
                                                "message": "닉네임은 2~20자의 한글, 영문, 숫자만 사용할 수 있습니다.",
                                                "field": "nickname"
                                            }
                                            """))),
            @ApiResponse(responseCode = "409",
                    content = @Content(
                            examples = @ExampleObject(name = "닉네임 중복", value = """
                                            {
                                                "code": "USER_ALREADY_EXISTS",
                                                "message": "이미 사용 중인 닉네임입니다."
                                            }
                                            """)))})
    @PatchMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    public UserInfoResponse updateMyInfo(
            @Valid @RequestBody UpdateUserRequest request,
            @RequestAttribute Long userId
    ) {
        return userService.updateUserInfo(userId, request);
    }

    @Operation(summary = "회원 탈퇴", description = "로그인한 사용자의 회원 정보를 영구 삭제합니다.")
    @DeleteMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMyAccount(@RequestAttribute Long userId) {
        userService.deleteUser(userId);
    }

    @Operation(summary = "비밀번호 변경")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400",
                    content = @Content(
                            examples = {
                                    @ExampleObject(name = "유효하지 않은 인증 정보", value = """
                                            {
                                                "code": "INVALID_CREDENTIALS",
                                                "message": "인증 정보가 올바르지 않습니다."
                                            }
                                            """),
                                    @ExampleObject(name = "유효하지 않은 비밀번호 형식", value = """
                                            {
                                                "code": "INVALID_USER_INPUT",
                                                "message": "비밀번호는 8자 이상, 영문과 숫자를 포함해야 합니다.",
                                                "field": "newPassword"
                                            }
                                            """)
                            }))})
    @PutMapping("/me/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changeMyPassword(
            @Valid @RequestBody ChangePasswordRequest request,
            @RequestAttribute Long userId
    ) {
        userService.changePassword(userId, request);
    }

    @Operation(summary = "이메일 중복 검사", description = "회원 가입시 해당 이메일 사용 가능 여부를 검사합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "409",
                    content = @Content(
                            examples = @ExampleObject(name = "이메일 중복", value = """
                                            {
                                                "code": "USER_ALREADY_EXISTS",
                                                "message": "이미 사용 중인 이메일입니다."
                                            }
                                            """)))})
    @GetMapping("/emails/availability")
    @ResponseStatus(HttpStatus.OK)
    public AvailabilityResponse validateEmail(@Valid @ModelAttribute EmailAvailabilityRequest request) {
        return userService.validateEmail(request.email());
    }

    @Operation(summary = "닉네임 중복 검사", description = "회원 가입시 해당 닉네임 사용 가능 여부를 검사합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "409",
                    content = @Content(
                            examples = @ExampleObject(name = "닉네임 중복", value = """
                                            {
                                                "code": "USER_ALREADY_EXISTS",
                                                "message": "이미 사용 중인 닉네임입니다."
                                            }
                                            """)))})
    @GetMapping("/nicknames/availability")
    @ResponseStatus(HttpStatus.OK)
    public AvailabilityResponse validateNickname(@Valid @ModelAttribute NicknameAvailabilityRequest request) {
        return userService.validateNickname(request.nickname());
    }
}
