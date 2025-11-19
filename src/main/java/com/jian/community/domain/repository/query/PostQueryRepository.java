package com.jian.community.domain.repository.query;

import com.jian.community.domain.dto.CursorPage;
import com.jian.community.domain.model.Post;

import java.time.LocalDateTime;

public interface PostQueryRepository {

    CursorPage<Post> findAllAndIsDeletedFalseOrderByCreatedAtDesc(LocalDateTime cursor, int pageSize);
}
