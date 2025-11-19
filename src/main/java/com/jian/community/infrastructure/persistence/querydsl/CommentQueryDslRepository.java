package com.jian.community.infrastructure.persistence.querydsl;

import com.jian.community.domain.dto.CursorPage;
import com.jian.community.domain.model.Comment;
import com.jian.community.domain.model.QComment;
import com.jian.community.domain.repository.query.CommentQueryRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class CommentQueryDslRepository implements CommentQueryRepository {

    private final JPAQueryFactory queryFactory;
    private final QComment qComment = QComment.comment;

    @Override
    public CursorPage<Comment> findAllByPostIdAndIsDeletedFalseOrderByCreatedAtDesc(Long postId, LocalDateTime cursor, int pageSize) {
        List<Comment> results = queryFactory.selectFrom(qComment)
                .where(
                        qComment.post.id.eq(postId),
                        qComment.isDeleted.isFalse(),
                        cursor != null ?
                                qComment.createdAt.before(cursor)
                                : null // 조건 없음
                )
                .orderBy(qComment.createdAt.desc())
                .limit(pageSize + 1)
                .fetch();

        boolean hasNext = results.size() > pageSize;
        List<Comment> content = hasNext ? results.subList(0, pageSize) : results;

        return new CursorPage<>(content, hasNext);
    }
}
