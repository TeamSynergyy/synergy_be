package com.seoultech.synergybe.domain.project.dto.response;

import com.seoultech.synergybe.domain.common.PageInfo;

import java.util.List;

public record GetListProjectResponse(
        List<GetProjectResponse> getProjectResponses,
        PageInfo pageInfo
) {
}
