package com.jian.community.domain.repository.crud;

import com.jian.community.global.exception.ErrorMessage;
import com.jian.community.application.exception.ResourceNotFoundException;
import com.jian.community.domain.model.Post;

import java.util.Optional;

public interface PostRepository {

    Post save(Post post);

    Optional<Post> findByIdAndIsDeletedFalse(Long postId);

    default Post findByIdAndIsDeletedFalseOrThrow(Long postId) {
        return findByIdAndIsDeletedFalse(postId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessage.POST_NOT_EXISTS));
    }
}
