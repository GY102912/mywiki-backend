package com.jian.community.presentation.controller;

import com.jian.community.application.service.PostLikeService;
import com.jian.community.application.service.PostQueryService;
import com.jian.community.application.service.PostService;
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

import java.time.LocalDateTime;

@Tag(name = "Post", description = "게시글 관련 API")
@RestController
@RequestMapping("/posts")
@AllArgsConstructor
public class PostController {

    private final PostQueryService postQueryService;
    private final PostService postService;
    private final PostLikeService postLikeService;

    @Operation(
            summary = "게시글 목록 조회",
            description = "커서 기반 페이징 방식으로 게시글을 최대 10개씩 조회합니다. "
                    + "cursor를 입력하지 않으면 첫 페이지를 반환합니다."
    )
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public CursorResponse<PostResponse> getPosts(
            @RequestParam(required = false) LocalDateTime cursor
    ) {
        return postQueryService.getPosts(cursor);
    }

    @Operation(
            summary = "게시글 상세 조회",
            description = "게시글 상세 정보와 댓글 목록의 첫 페이지(최대 10개)가 함께 반환됩니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404",
                    content = @Content(
                            examples = @ExampleObject(name = "존재하지 않는 게시글", value = """
                                            {
                                                "code": "POST_NOT_EXISTS",
                                                "message": "게시글을 찾을 수 없습니다."
                                            }
                                            """)))})
    @GetMapping("/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public PostDetailResponse getPostDetail(
            @PathVariable Long postId,
            @RequestAttribute Long userId
    ) {
        return postQueryService.getPostDetail(postId, userId);
    }

    @Operation(summary = "게시글 생성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400",
                    content = @Content(
                            examples = {
                                    @ExampleObject(name = "유효하지 않은 게시글 제목 형식", value = """
                                            {
                                                "code": "INVALID_USER_INPUT",
                                                "message": "게시글 제목은 최대 26자까지 입력할 수 있습니다.",
                                                "field": "title"
                                            }
                                            """),
                                    @ExampleObject(name = "유효하지 않은 이미지 형식", value = """
                                            {
                                                "code": "INVALID_IMAGE_FORMAT",
                                                "message": "지원하지 않는 이미지 형식입니다."
                                            }
                                            """)
                            })),
            @ApiResponse(responseCode = "413",
                    content = @Content(
                            examples = @ExampleObject(name = "이미지 용량 초과", value = """
                                            {
                                                "code": "IMAGE_TOO_LARGE",
                                                "message": "이미지 용량이 너무 큽니다. 5MB 이하로 업로드해주세요."
                                            }
                                            """)))})
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PostIdResponse createPost(
            @Valid @RequestBody CreatePostRequest request,
            @RequestAttribute Long userId
    ) {
        return postService.createPost(userId, request);
    }

    @Operation(summary = "게시글 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400",
                    content = @Content(
                            examples = {
                                    @ExampleObject(name = "유효하지 않은 게시글 제목 형식", value = """
                                            {
                                                "code": "INVALID_USER_INPUT",
                                                "message": "게시글 제목은 최대 26자까지 입력할 수 있습니다.",
                                                "field": "title"
                                            }
                                            """),
                                    @ExampleObject(name = "유효하지 않은 이미지 형식", value = """
                                            {
                                                "code": "INVALID_IMAGE_FORMAT",
                                                "message": "지원하지 않는 이미지 형식입니다."
                                            }
                                            """)
                            })),
            @ApiResponse(responseCode = "404",
                    content = @Content(
                            examples = @ExampleObject(name = "존재하지 않는 게시글", value = """
                                            {
                                                "code": "POST_NOT_EXISTS",
                                                "message": "게시글을 찾을 수 없습니다."
                                            }
                                            """))),
            @ApiResponse(responseCode = "413",
                    content = @Content(
                            examples = @ExampleObject(name = "이미지 용량 초과", value = """
                                            {
                                                "code": "IMAGE_TOO_LARGE",
                                                "message": "이미지 용량이 너무 큽니다. 5MB 이하로 업로드해주세요."
                                            }
                                            """)))})
    @PutMapping("/{postId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updatePost(
            @PathVariable Long postId,
            @Valid @RequestBody UpdatePostRequest request,
            @RequestAttribute Long userId
    ) {
        postService.updatePost(userId, postId, request);
    }

    @Operation(summary = "게시글 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400",
                    content = @Content(
                            examples = @ExampleObject(name = "존재하지 않는 게시글", value = """
                                            {
                                                "code": "POST_NOT_EXISTS",
                                                "message": "게시글을 찾을 수 없습니다."
                                            }
                                            """)))})
    @DeleteMapping("/{postId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(
            @PathVariable Long postId,
            @RequestAttribute Long userId
    ) {
        postService.deletePost(userId, postId);
    }

    @Operation(summary = "게시글 좋아요")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400",
                    content = @Content(
                            examples = @ExampleObject(name = "존재하지 않는 게시글", value = """
                                            {
                                                "code": "POST_NOT_EXISTS",
                                                "message": "게시글을 찾을 수 없습니다."
                                            }
                                            """))),
            @ApiResponse(responseCode = "409",
                    content = @Content(
                            examples = @ExampleObject(name = "닉네임 중복", value = """
                                            {
                                                "code": "POST_LIKE_ALREADY_EXISTS",
                                                "message" : "이미 좋아요한 게시글입니다."
                                            }
                                            """)))})
    @PostMapping("/{postId}/likes")
    @ResponseStatus(HttpStatus.CREATED)
    public void createPostLike(
            @PathVariable Long postId,
            @RequestAttribute Long userId
    ) {
        postLikeService.createPostLike(postId, userId);
    }

    @Operation(summary = "게시글 좋아요 취소")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400",
                    content = @Content(
                            examples = @ExampleObject(name = "존재하지 않는 게시글", value = """
                                            {
                                                "code": "POST_NOT_EXISTS",
                                                "message": "게시글을 찾을 수 없습니다."
                                            }
                                            """)))})
    @DeleteMapping("/{postId}/likes")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePostLike(
            @PathVariable Long postId,
            @RequestAttribute Long userId
    ) {
        postLikeService.deletePostLike(postId, userId);
    }
}
