package com.seoultech.synergybe.domain.post.repository;

import com.seoultech.synergybe.domain.post.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query(value = "SELECT * FROM post WHERE post_id < :postId AND is_deleted = 0 ORDER BY post_id DESC LIMIT 10", nativeQuery = true)
    List<Post> findAllByEndId(@Param("postId") Long postId);

    @Query(value = "SELECT * FROM post WHERE user_id = :userId AND is_deleted = 0", nativeQuery = true)
    List<Post> findAllByUserId(@Param("userId") String userId);

    Page<Post> findAll(Specification<Post> spec, Pageable pageable);

    @Query(value = "SELECT * FROM (SELECT * FROM post WHERE user_id = :userId) p WHERE post_id < :end AND is_deleted = 0 ORDER BY post_id DESC LIMIT 10", nativeQuery = true)
    List<Post> findAllByFollowingIdAndEndId(@Param("userId") String userId, @Param("end") Long end);

    @Query(value = "SELECT count(*) FROM post WHERE post_id < :end AND is_deleted = 0", nativeQuery = true)
    int countPostList(@Param("end") Long end);

    @Query(value = "SELECT count(*) FROM (SELECT * FROM post WHERE user_id = :userId AND is_deleted = 0) p WHERE post_id < :end AND is_deleted = 0", nativeQuery = true)
    int countFeed(@Param("end") Long end);

    @Query(value =
            "SELECT p.post_id, p.create_at, p.update_at, p.author_name, p.content, p.is_deleted, p.title, p.user_id, p.thumbnail_image_id, COUNT(pl.post_like_id) AS like_count " +
            "FROM post p " +
            "LEFT JOIN post_like pl ON p.post_id = pl.post_id " +
            "WHERE p.create_at >= DATE_SUB(NOW(), INTERVAL 1 WEEK) AND p.is_deleted = 0 " +
            "GROUP BY p.post_id " +
            "ORDER BY like_count DESC " +
            "LIMIT 5;"
            , nativeQuery = true)
    List<Post> findAllByLikeAndDate();
}
