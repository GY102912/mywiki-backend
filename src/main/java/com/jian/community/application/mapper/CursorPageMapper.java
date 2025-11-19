package com.jian.community.application.mapper;

import com.jian.community.domain.dto.CursorPage;
import com.jian.community.domain.model.MinimalEntity;
import com.jian.community.presentation.dto.CursorResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;

public class CursorPageMapper {

    public static <E extends MinimalEntity, R> CursorResponse<R> toCursorResponse(
            CursorPage<E> page,
            Function<E, R> mapper
    ) {
        LocalDateTime nextCursor = page.hasNext() ? page.content().getLast().getCreatedAt() : null;
        List<R> items = page.content().stream()
                .map(mapper)
                .toList();

        return new CursorResponse<>(items, nextCursor, page.hasNext());
    }
}
