package com.seoultech.synergybe.domain.post.repository;

import com.seoultech.synergybe.domain.post.Post;

import java.util.List;

public interface PostRepositoryCustom {
    List<Post> findAllByCreateAtAndLimit(Long offset);

    List<Post> findAllRecentByCount(Long offset);

    List<Post> findAllByUserId(Long userId);

    List<Post> findMyLikedPostList(Long userId);

    Long countSize();

    Long totalSizeUser(String userId);

    List<Post> findAllByFollowerIds(List<Long> userIds);

    List<Post> findAllByIdsAndEndId(Long userId, String offsetToken);

    List<Post> findAllByLikeAndDate();
}
