package com.seoultech.synergybe.domain.follow.dto.response;

import com.seoultech.synergybe.domain.follow.Follow;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class FollowResponse {
    private String followingId;

    public static FollowResponse from(Follow updatedFollow) {
        return new FollowResponse(updatedFollow.getFollowing().getUserId());
    }
}
