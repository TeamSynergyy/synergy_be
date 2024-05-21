package com.seoultech.synergybe.domain.post.data;

import com.seoultech.synergybe.domain.post.Post;
import com.seoultech.synergybe.domain.post.presentation.dto.response.GetPostResponse;

import java.util.List;

public interface PostRepositoryCustom {
//    List<Post> findAllByCreateAtAndLimit(Long offset);

    List<Post> findAllRecentByCount(Long offset);

    List<Post> findAllByUserId(String userId);

//    List<Post> findAllByFollowingIdAndEndSeq(String followingId, Long end);

    Long countSize();

    Long totalSizeUser(String userId);

    List<Post> WeekBest();

    List<Post> findAllByFollowerIds(List<String> userIds);
}
