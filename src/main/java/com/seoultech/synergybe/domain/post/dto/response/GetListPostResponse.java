package com.seoultech.synergybe.domain.post.dto.response;

import com.seoultech.synergybe.domain.common.PageInfo;

import java.util.List;

public record GetListPostResponse(
        List<GetPostResponse> getPostResponses,
        PageInfo pageInfo
) {

}
