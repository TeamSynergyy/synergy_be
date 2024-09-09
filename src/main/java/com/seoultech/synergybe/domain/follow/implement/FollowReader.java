package com.seoultech.synergybe.domain.follow.implement;

import com.seoultech.synergybe.domain.follow.repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FollowReader {
    private final FollowRepository followRepository;

    public List<Long> readFollowingIds(String userToken) {
        return followRepository.findFollowingIdsByFollowerToken(userToken);
    }
}
