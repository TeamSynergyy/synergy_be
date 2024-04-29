package com.seoultech.synergybe.domain.common.paging;

import java.util.List;

public record ListResponse<T>(
        List<T> contents,
        Long totalElements,
        Boolean hasNext
) {
    public ListResponse(List<T> contents, Boolean hasNext) {
        this(
                contents,
                (long) contents.size(),
                hasNext
        );
    }

    public ListResponse(List<T> contents) {
        this(
                contents,
                (long) contents.size(),
                false
        );
    }
}
