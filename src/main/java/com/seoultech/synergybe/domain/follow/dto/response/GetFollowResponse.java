package com.seoultech.synergybe.domain.follow.dto.response;


import lombok.Builder;

@Builder
public record GetFollowResponse(
        String followingId
) {
}
