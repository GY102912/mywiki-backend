package com.jian.community.application.mapper;

import com.jian.community.domain.model.Post;
import com.jian.community.domain.model.User;
import com.jian.community.presentation.dto.CommentResponse;
import com.jian.community.presentation.dto.CursorResponse;
import com.jian.community.presentation.dto.PostDetailResponse;
import com.jian.community.presentation.dto.PostResponse;

public class PostDtoMapper {

    public static PostResponse toPostResponse(
            Post post, User writer,
            Integer likeCount, Integer commentCount, Integer viewCount
    ) {
        return new PostResponse(
                post.getId(),
                post.getTitle(),
                writer.getNickname(),
                writer.getProfileImageUrl(),
                likeCount,
                commentCount,
                viewCount,
                post.getCreatedAt(),
                post.getUpdatedAt()
        );
    }

    public static PostDetailResponse toPostDetailResponse(
            Post post, User writer, Boolean isWriter, Boolean isLiked,
            Integer likeCount, Integer commentCount, Integer viewCount,
            CursorResponse<CommentResponse> commentPreview
    ) {

        return new PostDetailResponse(
                post.getId(), post.getTitle(),
                writer.getNickname(), writer.getProfileImageUrl(),
                isWriter, isLiked,
                likeCount, commentCount, viewCount,
                post.getCreatedAt(), post.getUpdatedAt(), post.getContent(), post.getPostImageUrls(),
                commentPreview
        );
    }
}
