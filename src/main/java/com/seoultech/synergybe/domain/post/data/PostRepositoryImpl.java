package com.seoultech.synergybe.domain.post.data;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.seoultech.synergybe.domain.post.Post;
import com.seoultech.synergybe.domain.post.presentation.dto.response.GetPostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.querydsl.core.types.Projections.constructor;
import static com.seoultech.synergybe.domain.post.QPost.post;
import static com.seoultech.synergybe.domain.postlike.QPostLike.postLike;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {
    private final JPAQueryFactory queryFactory;


    @Override
    public List<Post> findAllByCreateAtAndLimit(Long offset) {
        return queryFactory
                .selectFrom(post)
                .orderBy(post.createAt.desc())
                .limit(10)
                .offset(offset)
                .fetch();
    }

    @Override
    public List<Post> findAllByUserId(String userId) {
        return queryFactory
                .selectFrom(post)
                .where(post.user.id.eq(userId))
                .fetch();
    }

    @Override
    public List<Post> findAllByFollowingIdAndEndSeq(String followingId, Long end) {
        return queryFactory
                .selectFrom(post)
                .where(post.user.id.eq(followingId))
                .orderBy(post.createAt.desc())
                .fetch();
    }

    @Override
    public int countTotalPostSize() {
        return queryFactory
                .selectFrom(post)
                .fetch().size();
    }

    @Override
    public List<GetPostResponse> findAllByMostLikedAndRecentOneWeek() {
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);
        return queryFactory
                .select(
                        constructor(GetPostResponse.class,
                                post.id,
                                post.createAt,
                                post.updateAt,
                                post.authorName,
                                post.content.content,
                                post.title.title,
                                post.user.id,
                                postLike.id.count().as("likes")
                        )
                )
                .from(post)
                .leftJoin(postLike).on(post.id.eq(postLike.post.id))
                .where(post.createAt.goe(oneWeekAgo))
                .groupBy(post.id)
                .orderBy(postLike.id.count().desc())
                .limit(10)
                .fetch();
    }
}
