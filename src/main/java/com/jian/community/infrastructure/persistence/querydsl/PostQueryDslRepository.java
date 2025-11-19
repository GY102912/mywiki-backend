package com.jian.community.infrastructure.persistence.querydsl;

import com.jian.community.domain.dto.CursorPage;
import com.jian.community.domain.model.Post;
import com.jian.community.domain.model.QPost;
import com.jian.community.domain.repository.query.PostQueryRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class PostQueryDslRepository implements PostQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final QPost qPost = QPost.post;

    @Override
    public CursorPage<Post> findAllAndIsDeletedFalseOrderByCreatedAtDesc(LocalDateTime cursor, int pageSize) {
        List<Post> results = jpaQueryFactory.selectFrom(qPost)
                .where(
                        qPost.isDeleted.isFalse(),
                        cursor != null ?
                                qPost.createdAt.before(cursor)
                                : null // 조건 없음
                )
                .orderBy(qPost.createdAt.desc())
                .limit(pageSize + 1)
                .fetch();

        boolean hasNext = results.size() > pageSize;
        List<Post> content = hasNext ? results.subList(0, pageSize) : results;

        return new CursorPage<>(content, hasNext);
    }
}
