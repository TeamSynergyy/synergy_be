package com.seoultech.synergybe.domain.post.infrastructure;

import com.seoultech.synergybe.domain.post.Post;

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
