package com.jian.community.application.service;

import com.jian.community.application.mapper.CommentDtoMapper;
import com.jian.community.application.mapper.CursorPageMapper;
import com.jian.community.domain.dto.CursorPage;
import com.jian.community.domain.repository.query.CommentQueryRepository;
import com.jian.community.global.exception.ErrorMessage;
import com.jian.community.application.exception.ResourceNotFoundException;
import com.jian.community.domain.exception.UnauthorizedWriterException;
import com.jian.community.domain.model.*;
import com.jian.community.domain.repository.crud.CommentRepository;
import com.jian.community.domain.repository.crud.PostRepository;
import com.jian.community.domain.repository.crud.UserRepository;
import com.jian.community.presentation.dto.CommentResponse;
import com.jian.community.presentation.dto.CreateCommentRequest;
import com.jian.community.presentation.dto.CursorResponse;
import com.jian.community.presentation.dto.UpdateCommentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class CommentService {

    private static final int COMMENT_PAGE_SIZE = 10;

    private final CommentRepository commentRepository;
    private final CommentQueryRepository commentQueryRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public CursorResponse<CommentResponse> getComments(Long postId, Long userId, LocalDateTime cursor) {
        Post post = postRepository.findByIdAndIsDeletedFalseOrThrow(postId);
        CursorPage<Comment> page = commentQueryRepository
                .findAllByPostIdAndIsDeletedFalseOrderByCreatedAtDesc(post.getId(), cursor, COMMENT_PAGE_SIZE);

        User reader = userRepository.findByIdAndIsDeletedFalseOrThrow(userId);

        return CursorPageMapper.toCursorResponse(page, comment -> {
            User writer = userRepository.findByIdAndIsDeletedFalseOrThrow(comment.getUser().getId());
            boolean isWriter = comment.isWrittenBy(reader);

            return CommentDtoMapper.toCommentResponse(comment, writer, isWriter);
        });
    }

    @Transactional(readOnly = true)
    public CursorResponse<CommentResponse> getRecentComments(Long postId, Long userId) {
        return getComments(postId, userId, null);
    }

    @Transactional
    public CommentResponse creatComment(Long postId, Long userId, CreateCommentRequest request) {
        Post post = postRepository.findByIdAndIsDeletedFalseOrThrow(postId);
        User writer = userRepository.findByIdAndIsDeletedFalseOrThrow(userId);

        Comment comment = Comment.of(post, writer, request.content());
        Comment saved = commentRepository.save(comment);

        return new CommentResponse(
                saved.getId(),
                writer.getNickname(),
                writer.getProfileImageUrl(),
                true,
                saved.getContent(),
                saved.getCreatedAt(),
                saved.getUpdatedAt()
        );
    }

    @Transactional
    public void updateComment(Long postId, Long commentId, Long userId, UpdateCommentRequest request) {
        Post post = postRepository.findByIdAndIsDeletedFalseOrThrow(postId);
        Comment comment = commentRepository.findByIdAndIsDeletedFalseOrThrow(commentId);
        User writer = userRepository.findByIdAndIsDeletedFalseOrThrow(userId);

        validateCommandPermission(post, comment, writer);

        comment.update(request.content());
    }

    @Transactional
    public void deleteComment(Long postId, Long commentId, Long userId) {
        Post post = postRepository.findByIdAndIsDeletedFalseOrThrow(postId);
        Comment comment = commentRepository.findByIdAndIsDeletedFalseOrThrow(commentId);
        User writer = userRepository.findByIdAndIsDeletedFalseOrThrow(userId);

        validateCommandPermission(post, comment, writer);

        comment.delete();
    }

    private void validateCommandPermission(Post post, Comment comment, User writer) {
        if (!comment.isBelongsTo(post)) {
            throw new ResourceNotFoundException(ErrorMessage.POST_NOT_EXISTS);
        }
        if (!comment.isWrittenBy(writer)) {
            throw new UnauthorizedWriterException(ErrorMessage.UNAUTHORIZED_COMMENT_WRITER);
        }
    }
}
