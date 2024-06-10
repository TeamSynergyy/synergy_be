package com.seoultech.synergybe.domain.follow.service;

import com.seoultech.synergybe.domain.common.generator.IdGenerator;
import com.seoultech.synergybe.domain.common.generator.IdPrefix;
import com.seoultech.synergybe.domain.common.paging.ListResponse;
import com.seoultech.synergybe.domain.follow.Follow;
import com.seoultech.synergybe.domain.follow.FollowStatus;
import com.seoultech.synergybe.domain.follow.dto.request.CreateFollowRequest;
import com.seoultech.synergybe.domain.follow.dto.response.GetFollowResponse;
import com.seoultech.synergybe.domain.follow.exception.FollowNotFoundException;
import com.seoultech.synergybe.domain.follow.repository.FollowRepository;
import com.seoultech.synergybe.domain.notification.service.NotificationService;
import com.seoultech.synergybe.domain.user.User;
import com.seoultech.synergybe.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class FollowService {
    private final FollowRepository followRepository;
    private final UserService userService;
    private final NotificationService notificationService;
    private final IdGenerator idGenerator;

    public List<String> findFollowingIdsByUserId(String userId) {
        return followRepository.findFollowingIdsByFollowerId(userId);
    }

    /**
     *
     * @param user 신청한 유저
     * @param followingId 신청 받은 유저
     * @param type
     * @return
     */
    @Transactional
    public GetFollowResponse updateFollow(User user, String followingId, CreateFollowRequest type) {
        FollowStatus status;
        if (type.followType().equals("follow")) {
            status = FollowStatus.FOLLOW;
        } else {
            status = FollowStatus.UNFOLLOW;
        }

        try {
            Follow updatedFollow = update(user, followingId, status);
            GetFollowResponse getFollowResponse = GetFollowResponse.builder().build();

            return getFollowResponse;
        } catch (Exception e) {
            throw new FollowNotFoundException("존재하지 않는 팔로우입니다.");
        }



    }

    public synchronized Follow update(User user, String followingId, FollowStatus status) {
        Optional<Follow> followOptional = followRepository.findByFollowerIdAndFollowingId(user.getId(), followingId);

        if (followOptional.isPresent()) {
            followOptional.get().updateStatus(status);
            User following = userService.getUser(followingId);
//            notificationService.send(following, NotificationType.FOLLOW, "팔로우 신청이 완료되었습니다.", Long.valueOf(followingId));

            return followOptional.get();
        } else {
            User following = userService.getUser(followingId);
            String followId = idGenerator.generateId(IdPrefix.FOLLOW);
            Follow follow = Follow.builder()
                    .id(followId)
                    .follower(user)
                    .following(following)
                    .build();
//            notificationService.send(following, NotificationType.FOLLOW, "팔로우 신청이 완료되었습니다.", Long.valueOf(followingId));

            return followRepository.saveAndFlush(follow);
        }
    }

    public List<String> getFollowerIdList(String userId) {
        return followRepository.findFollowerIdsByFollowingId(userId);
    }

    public List<String> getFollowingIdList(String userId) {
        return followRepository.findFollowingIdsByFollowerId(userId);
    }

    public ListResponse<String> getFollowerIds(String userId) {
        List<String> getFollowerIdList = getFollowerIdList(userId);

        ListResponse<String> getUserIdListResponses = new ListResponse(getFollowerIdList);

        return getUserIdListResponses;
    }

    public ListResponse<String> getFollowingIds(String userId) {
        List<String> getFollowingIdList = getFollowingIdList(userId);

        ListResponse<String> getUserIdListResponses = new ListResponse(getFollowingIdList);

        return getUserIdListResponses;
    }
}
