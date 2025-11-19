package com.jian.community.application.service;

import com.jian.community.global.exception.ErrorMessage;
import com.jian.community.application.exception.ResourceNotFoundException;
import com.jian.community.domain.exception.UnauthorizedWriterException;
import com.jian.community.domain.model.Post;
import com.jian.community.domain.model.PostLike;
import com.jian.community.domain.model.User;
import com.jian.community.domain.repository.crud.PostLikeRepository;
import com.jian.community.domain.repository.crud.PostRepository;
import com.jian.community.domain.repository.crud.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public void createPostLike(Long postId, Long userId) {
        Post post = postRepository.findByIdAndIsDeletedFalseOrThrow(postId);
        User user = userRepository.findByIdAndIsDeletedFalseOrThrow(userId);

        PostLike postLike = PostLike.of(post, user);
        postLikeRepository.save(postLike);
    }

    @Transactional
    public void deletePostLike(Long postId, Long userId) {
        Post post = postRepository.findByIdAndIsDeletedFalseOrThrow(postId);
        User user = userRepository.findByIdAndIsDeletedFalseOrThrow(userId);

        postLikeRepository.findByIdPostIdAndIdUserIdAndIsDeletedFalse(postId, userId)
                .ifPresent((postLike -> {
                    validateCommandPermission(post, user, postLike);
                    postLike.delete();
                }));
    }

    private void validateCommandPermission(Post post, User user, PostLike postLike) {
        if (!postLike.isBelongsTo(post)) {
            throw new ResourceNotFoundException(ErrorMessage.POST_NOT_EXISTS);
        }
        if (!postLike.isLikedBy(user)) {
            throw new UnauthorizedWriterException(ErrorMessage.UNAUTHORIZED_POST_LIKE_USER);
        }
    }
}
