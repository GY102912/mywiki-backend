package com.jian.community.infrastructure.persistence.jpa;

import com.jian.community.domain.model.PostView;
import com.jian.community.domain.repository.crud.PostViewRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostViewJpaRepository extends PostViewRepository, JpaRepository<PostView,Long> {

    @Override
    PostView save(PostView postView);

    @Override
    Optional<PostView> findByPostId(Long postId);

    @Override
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE tb_post_view SET count = count + 1 WHERE post_id = :postId", nativeQuery = true)
    void increaseCount(@Param("postId") Long postId);
}