package com.seoultech.synergybe.domain.follow.repository;

import com.seoultech.synergybe.domain.follow.Follow;

import java.util.List;

public interface FollowRepositoryCustom {
    List<Long> findFollowingIdsByFollowerToken(String followerToken);
    List<Long> findFollowerIdsByFollowingToken(String followingToken);
    Follow findByFollowerTokenAndFollowingToken(String userToken, String followingToken);
}
