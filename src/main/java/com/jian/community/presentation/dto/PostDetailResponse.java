package com.jian.community.presentation.dto;

import java.time.LocalDateTime;
import java.util.List;

public record PostDetailResponse(

        Long id,
        String title,
        String writerNickname,
        String writerProfileImageUrl,
        Boolean isWriter,
        Boolean isLiked,
        Integer likeCount,
        Integer commentCount,
        Integer viewCount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String content,
        List<String> postImageUrls,
        CursorResponse<CommentResponse> commentsPreview
) {
}
