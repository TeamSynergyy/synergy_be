package com.seoultech.synergybe.domain.follow.implement;

import com.seoultech.synergybe.domain.follow.repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FollowReader {
    private final FollowRepository followRepository;

    public List<String> readFollowingIds(String userId) {
        return followRepository.findFollowingIdsByFollowerId(userId);
    }
}
