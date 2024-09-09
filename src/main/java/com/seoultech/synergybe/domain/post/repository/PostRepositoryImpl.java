package com.seoultech.synergybe.domain.post.repository;

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
                .where(post.user.userToken.eq(userId))
                .fetchOne();
    }

    @Override
    public List<Post> findAllByCreateAtAndLimit(Long offset) {
        return queryFactory
                .selectFrom(post)
                .where(post.id.lt(offset)
                        .and(post.isDeleted.isDeleted.eq(false)))
                .orderBy(post.createAt.desc())
                .limit(20)
                .fetch();
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
    public List<Post> findAllByUserId(Long userId) {
        return queryFactory
                .selectFrom(post)
                .where(post.user.id.eq(userId))
                .fetch();
    }

    @Override
    public List<Post> findMyLikedPostList(Long userId) {
        return queryFactory
                .selectFrom(post)
                .leftJoin(postLike).on(post.id.eq(postLike.post.id)) // post와 postLike를 조인
                .where(postLike.user.id.eq(userId) // 해당 사용자가 좋아요한 게시글
                        .and(post.isDeleted.isDeleted.eq(false))) // 삭제되지 않은 게시글
                .groupBy(post.id) // 게시글별 그룹화
                .orderBy(post.createAt.desc()) // 최신순으로 정렬
                .limit(5) // 상위 5개의 게시글만 조회
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
    public List<Post> findAllByFollowerIds(List<Long> userIds) {
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);

        return queryFactory
                .selectFrom(post)
                .where(post.user.id.in(userIds)
                        .and(post.createAt.goe(oneWeekAgo)))
                .orderBy(post.createAt.desc())
                .limit(10)
                .fetch();
    }

    @Override
    public List<Post> findAllByIdsAndEndId(Long userId, String offsetToken) {
        return queryFactory
                .selectFrom(post)
                .where(post.user.id.eq(userId) // userId로 필터링
                        .and(post.id.lt(Long.valueOf(offsetToken))) // offsetToken보다 작은 post_id 조회
                        .and(post.isDeleted.isDeleted.eq(false))) // is_deleted가 false인 게시물만 조회
                .orderBy(post.id.desc()) // post_id 기준 내림차순 정렬
                .limit(10) // 상위 10개 게시물만 조회
                .fetch();
    }

    @Override
    public List<Post> findAllByLikeAndDate() {
        return queryFactory
                .selectFrom(post)
                .leftJoin(postLike).on(post.id.eq(postLike.post.id)) // post와 postLike를 조인
                .where(post.isDeleted.isDeleted.eq(false)) // 삭제되지 않은 게시글
                .groupBy(post.id) // 게시글별 그룹화
                .orderBy(postLike.id.count().desc()) // 좋아요 수 기준으로 정렬
                .limit(20) // 상위 20개의 게시글만 조회
                .fetch();
    }
}
