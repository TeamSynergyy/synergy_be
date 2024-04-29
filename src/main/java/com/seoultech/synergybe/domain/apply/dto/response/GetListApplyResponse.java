package com.seoultech.synergybe.domain.apply.dto.response;

import com.seoultech.synergybe.domain.common.PageInfo;
import lombok.Builder;

import java.util.List;

@Builder
public record GetListApplyResponse(
        List<GetApplyResponse> getApplyResponses,
        PageInfo pageInfo
) {
}
