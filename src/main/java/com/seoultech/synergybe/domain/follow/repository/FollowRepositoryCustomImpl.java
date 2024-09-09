package com.seoultech.synergybe.domain.follow.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.seoultech.synergybe.domain.follow.Follow;
import com.seoultech.synergybe.domain.follow.FollowStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.seoultech.synergybe.domain.follow.QFollow.follow;

@RequiredArgsConstructor
@Repository
public class FollowRepositoryCustomImpl implements FollowRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Long> findFollowingIdsByFollowerToken(String followerToken) {
        return queryFactory
                .select(follow.follower.id)
                .where(
                        follow.follower.userToken.eq(followerToken).and(follow.status.eq(FollowStatus.FOLLOW)))
                .fetch();
    }

    @Override
    public List<Long> findFollowerIdsByFollowingToken(String followingToken) {
        return queryFactory
                .select(follow.follower.id)
                .where(
                        follow.following.userToken.eq(followingToken).and(follow.status.eq(FollowStatus.FOLLOW)))
                .fetch();
    }

    @Override
    public Follow findByFollowerTokenAndFollowingToken(String userToken, String followingToken) {
        return queryFactory
                .selectFrom(follow)
                .where(
                        follow.follower.userToken.eq(userToken).and(follow.following.userToken.eq(followingToken))
                )
                .fetchOne();
    }
}
