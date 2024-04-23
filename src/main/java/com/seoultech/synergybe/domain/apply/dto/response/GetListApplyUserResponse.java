package com.seoultech.synergybe.domain.apply.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record GetListApplyUserResponse(
        List<GetApplyUserResponse> applyUserResponses
) {
}
