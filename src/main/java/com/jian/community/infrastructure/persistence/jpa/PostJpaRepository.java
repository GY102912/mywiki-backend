package com.jian.community.infrastructure.persistence.jpa;

import com.jian.community.domain.model.Post;
import com.jian.community.domain.model.PostLikeId;
import com.jian.community.domain.repository.crud.PostRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostJpaRepository extends PostRepository, JpaRepository<Post, PostLikeId> {

    @Override
    Post save(Post post);

    @Override
    Optional<Post> findByIdAndIsDeletedFalse(Long postId);
}
