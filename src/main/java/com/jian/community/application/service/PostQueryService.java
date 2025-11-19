package com.jian.community.application.service;

import com.jian.community.application.mapper.CursorPageMapper;
import com.jian.community.application.mapper.PostDtoMapper;
import com.jian.community.domain.dto.CursorPage;
import com.jian.community.domain.event.PostViewEvent;
import com.jian.community.domain.model.*;
import com.jian.community.domain.repository.crud.*;
import com.jian.community.domain.repository.query.PostQueryRepository;
import com.jian.community.presentation.dto.CommentResponse;
import com.jian.community.presentation.dto.CursorResponse;
import com.jian.community.presentation.dto.PostDetailResponse;
import com.jian.community.presentation.dto.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PostQueryService {

    private static final int POST_PAGE_SIZE = 10;

    private final ApplicationEventPublisher eventPublisher;
    private final PostRepository postRepository;
    private final PostQueryRepository postQueryRepository;
    private final PostLikeRepository postLikeRepository;
    private final PostViewRepository postViewRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final CommentService commentService;

    public CursorResponse<PostResponse> getPosts(LocalDateTime cursor) {
        CursorPage<Post> page = postQueryRepository.findAllAndIsDeletedFalseOrderByCreatedAtDesc(cursor, POST_PAGE_SIZE);

        return CursorPageMapper.toCursorResponse(page, post -> {
            User writer = userRepository.findByIdAndIsDeletedFalseOrThrow(post.getUser().getId());
            List<PostLike> likes = postLikeRepository.findByIdPostIdAndIsDeletedFalse(post.getId());
            List<Comment> comments = commentRepository.findByPostIdAndIsDeletedFalse(post.getId());
            PostView view = postViewRepository.findByPostIdOrThrow(post.getId());

            return PostDtoMapper.toPostResponse(post, writer, likes.size(), comments.size(), view.getCount());
        });
    }

    public PostDetailResponse getPostDetail(Long postId, Long userId) {
        Post post = postRepository.findByIdAndIsDeletedFalseOrThrow(postId);
        User writer = userRepository.findByIdAndIsDeletedFalseOrThrow(post.getUser().getId());
        User reader = userRepository.findByIdAndIsDeletedFalseOrThrow(userId);

        List<PostLike> likes = postLikeRepository.findByIdPostIdAndIsDeletedFalse(postId);
        List<Comment> comments = commentRepository.findByPostIdAndIsDeletedFalse(postId);
        PostView view = postViewRepository.findByPostIdOrThrow(postId);
        CursorResponse<CommentResponse> commentPreview = commentService.getRecentComments(postId, userId);

        Boolean isWriter = post.isWrittenBy(reader);
        Boolean isLiked = likes.stream()
                .anyMatch(postLike -> postLike.getUser().getId().equals(reader.getId()));

        eventPublisher.publishEvent(new PostViewEvent(postId)); // 게시글 조회 이벤트 발행

        return PostDtoMapper.toPostDetailResponse(
                post, writer, isWriter, isLiked,
                likes.size(), comments.size(), view.getCount(),
                commentPreview
        );
    }
}
