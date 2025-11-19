package com.jian.community.infrastructure.persistence.jpa;

import com.jian.community.domain.model.Comment;
import com.jian.community.domain.repository.crud.CommentRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentJpaRepository extends CommentRepository, JpaRepository<Comment, Long> {

    @Override
    Comment save(Comment comment);

    @Override
    Optional<Comment> findByIdAndIsDeletedFalse(Long commentId);

    @Override
    List<Comment> findByPostIdAndIsDeletedFalse(Long postId);
}
