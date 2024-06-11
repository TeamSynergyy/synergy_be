package com.seoultech.synergybe.domain.apply.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import com.seoultech.synergybe.domain.apply.ApplyStatus;
import lombok.Builder;

@Builder
public record GetApplyResponse(
        String applyToken,
        String status
) {
    @QueryProjection
    public GetApplyResponse(String applyId, ApplyStatus status) {
        this(
                applyId,
                status.getName()
        );
    }
}
