package com.jian.community.application.service;

import com.jian.community.domain.repository.crud.PostRepository;
import com.jian.community.domain.repository.crud.PostViewRepository;
import com.jian.community.domain.repository.crud.UserRepository;
import com.jian.community.global.exception.ErrorMessage;
import com.jian.community.domain.exception.UnauthorizedWriterException;
import com.jian.community.domain.model.*;
import com.jian.community.presentation.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class PostService {

    private final PostRepository postRepository;
    private final PostViewRepository postViewRepository;
    private final UserRepository userRepository;

    public PostIdResponse createPost(Long userId, CreatePostRequest request) {
        User writer = userRepository.findByIdAndIsDeletedFalseOrThrow(userId);
        Post post = Post.of(
                writer,
                request.title(),
                request.content(),
                request.postImageUrls()
        );
        Long postId = postRepository.save(post).getId();

        PostView postView = PostView.from(postId);
        postViewRepository.save(postView);

        return new PostIdResponse(postId);
    }

    public void updatePost(Long userId, Long postId, UpdatePostRequest request) {
        Post post = postRepository.findByIdAndIsDeletedFalseOrThrow(postId);
        User writer = userRepository.findByIdAndIsDeletedFalseOrThrow(userId);

        validateCommandPermission(post, writer);

        post.update(request.title(), request.content(), request.postImageUrls());
    }

    public void deletePost(Long userId, Long postId) {
        Post post = postRepository.findByIdAndIsDeletedFalseOrThrow(postId);
        User writer = userRepository.findByIdAndIsDeletedFalseOrThrow(userId);

        validateCommandPermission(post, writer);

        post.delete();
    }

    private void validateCommandPermission(Post post, User writer) {
        if (!post.isWrittenBy(writer)) {
            throw new UnauthorizedWriterException(ErrorMessage.UNAUTHORIZED_POST_WRITER);
        }
    }
}
