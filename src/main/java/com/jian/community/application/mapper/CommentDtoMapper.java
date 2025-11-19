package com.jian.community.application.mapper;

import com.jian.community.domain.model.Comment;
import com.jian.community.domain.model.User;
import com.jian.community.presentation.dto.CommentResponse;

public class CommentDtoMapper {

    public static CommentResponse toCommentResponse(Comment comment, User writer, boolean isWriter) {
        return new CommentResponse(
                comment.getId(),
                writer.getNickname(),
                writer.getProfileImageUrl(),
                isWriter,
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }
}
