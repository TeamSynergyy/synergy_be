package com.seoultech.synergybe.domain.follow.service;

import com.seoultech.synergybe.domain.follow.Follow;
import com.seoultech.synergybe.domain.follow.FollowStatus;
import com.seoultech.synergybe.domain.follow.dto.request.FollowType;
import com.seoultech.synergybe.domain.follow.dto.response.FollowResponse;
import com.seoultech.synergybe.domain.follow.repository.FollowRepository;
import com.seoultech.synergybe.domain.notification.NotificationType;
import com.seoultech.synergybe.domain.notification.service.NotificationService;
import com.seoultech.synergybe.domain.user.User;
import com.seoultech.synergybe.domain.user.service.UserService;
import com.seoultech.synergybe.system.exception.NotExistFollowException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FollowService {
    private final FollowRepository followRepository;

    private final UserService userService;
    private final NotificationService notificationService;

    public List<String> findFollowingIdsByUserId(String userId) {
        return followRepository.findFollowingIdsByFollowerId(userId);
    }

    @Transactional
    public FollowResponse updateFollow(User user, String followingId, FollowType type) {
        FollowStatus status;
        if (type.getFollowType().equals("follow")) {
            status = FollowStatus.FOLLOW;
        } else {
            status = FollowStatus.UNFOLLOW;
        }

        try {
            Follow updatedFollow = update(user, followingId, status);

            return FollowResponse.from(updatedFollow);
        } catch (Exception e) {
            throw new NotExistFollowException();
        }



    }

    public synchronized Follow update(User user, String followingId, FollowStatus status) {
        Optional<Follow> followOptional = followRepository.findByFollowerIdAndFollowingId(user.getUserId(), followingId);

        if (followOptional.isPresent()) {
            followOptional.get().updateStatus(status);
            User following = userService.getUser(followingId);
            notificationService.send(following, NotificationType.FOLLOW, "팔로우 신청이 완료되었습니다.", Long.valueOf(followingId));

            return followOptional.get();
        } else {
            User following = userService.getUser(followingId);
            Follow follow = Follow.builder()
                    .follower(user)
                    .following(following)
                    .build();
            notificationService.send(following, NotificationType.FOLLOW, "팔로우 신청이 완료되었습니다.", Long.valueOf(followingId));

            return followRepository.saveAndFlush(follow);
        }
    }
}
