package com.jian.community.domain.dto;

import java.util.List;

public record CursorPage<T>(List<T> content, boolean hasNext) {
}
