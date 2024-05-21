package com.seoultech.synergybe.domain.post.data;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.seoultech.synergybe.domain.post.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.seoultech.synergybe.domain.post.QPost.post;
import static com.seoultech.synergybe.domain.postlike.QPostLike.postLike;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Long totalSizeUser(String userId) {
        return queryFactory
                .select(post.count())
                .from(post)
                .where(post.user.id.eq(userId))
                .fetchOne();
    }

    @Override
    public List<Post> findAllRecentByCount(Long offset) {
        return queryFactory
                .selectFrom(post)
                .orderBy(post.createAt.desc())
                .limit(offset)
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
    public Long countSize() {
        return queryFactory
                .select(post.count())
                .from(post)
                .fetchOne();
    }

    @Override
    public List<Post> WeekBest() {
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);
        return queryFactory
                .select(post
//                        constructor(GetPostResponse.class,
//                                post.id,
//                                post.createAt,
//                                post.updateAt,
//                                post.authorName,
//                                post.content.content,
//                                post.title.title,
//                                post.user.id,
//                                postLike.id.count().as("likes")
//                        )
                )
                .from(post)
                .leftJoin(postLike).on(post.id.eq(postLike.post.id))
                .where(post.createAt.goe(oneWeekAgo))
                .groupBy(post.id)
                .orderBy(postLike.id.count().desc())
                .limit(10)
                .fetch();
    }

    @Override
    public List<Post> findAllByFollowerIds(List<String> userIds) {
        return queryFactory
                .selectFrom(post)
                .where(post.user.id.in(userIds))
                .orderBy(post.createAt.desc())
                .limit(10)
                .fetch();
    }
}
