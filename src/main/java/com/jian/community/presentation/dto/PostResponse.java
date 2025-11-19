package com.jian.community.presentation.dto;

import java.time.LocalDateTime;

public record PostResponse(

        Long id,
        String title,
        String writerNickname,
        String writerProfileImageUrl,
        Integer likeCount,
        Integer commentCount,
        Integer viewCount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
