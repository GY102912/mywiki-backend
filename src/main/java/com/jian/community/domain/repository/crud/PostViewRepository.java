package com.jian.community.domain.repository.crud;

import com.jian.community.global.exception.ErrorMessage;
import com.jian.community.application.exception.ResourceNotFoundException;
import com.jian.community.domain.model.PostView;

import java.util.Optional;

public interface PostViewRepository {

    PostView save(PostView postView);

    Optional<PostView> findByPostId(Long postId);

    void increaseCount(Long postId);

    default PostView findByPostIdOrThrow(Long postId) {
        return findByPostId(postId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessage.POST_VIEW_NOT_EXISTS));
    }
}
