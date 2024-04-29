package com.seoultech.synergybe.domain.common;

public record PageInfo(
        long totalElements, boolean hasNext
) {
    public static PageInfo of(long totalElements, boolean hasNext) {
        return new PageInfo(totalElements, hasNext);
    }

    public static PageInfo of(long totalElements) {
        return new PageInfo(totalElements, false);
    }
}
