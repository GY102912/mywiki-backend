package com.jian.community.domain.repository.crud;

import com.jian.community.global.exception.ErrorMessage;
import com.jian.community.application.exception.ResourceNotFoundException;
import com.jian.community.domain.model.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {

    Comment save(Comment comment);

    Optional<Comment> findByIdAndIsDeletedFalse(Long commentId);

    List<Comment> findByPostIdAndIsDeletedFalse(Long postId);

    default Comment findByIdAndIsDeletedFalseOrThrow(Long commentId) {
        return findByIdAndIsDeletedFalse(commentId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessage.COMMENT_NOT_EXISTS));
    }
}
