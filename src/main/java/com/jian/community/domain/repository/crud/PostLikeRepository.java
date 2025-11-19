package com.jian.community.domain.repository.crud;

import com.jian.community.domain.model.PostLike;

import java.util.List;
import java.util.Optional;

public interface PostLikeRepository {

    PostLike save(PostLike postLike);

    List<PostLike> findByIdPostIdAndIsDeletedFalse(Long postId);

    Optional<PostLike> findByIdPostIdAndIdUserIdAndIsDeletedFalse(Long postId, Long userId);
}
