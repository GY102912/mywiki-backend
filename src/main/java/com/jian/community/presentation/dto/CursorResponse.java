package com.jian.community.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Getter
public class CursorResponse<T> {

    private List<T> items;
    private LocalDateTime nextCursor;
    private boolean hasNext;
}
