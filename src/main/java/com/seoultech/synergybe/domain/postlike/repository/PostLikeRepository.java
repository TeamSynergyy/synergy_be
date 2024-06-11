package com.seoultech.synergybe.domain.postlike.repository;

import com.seoultech.synergybe.domain.postlike.PostLike;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    @Query(value = "SELECT * FROM post_like WHERE user_id = :userId and post_id = :postId for update", nativeQuery = true)
    Optional<PostLike> findByUserIdAndPostId(@Param("userId") String userId, @Param("postId") String postId);

    @Query(value = "SELECT post_id FROM post_like WHERE status = 'LIKE' and user_id = :userId", nativeQuery = true)
    List<String> findPostIdsByUserId(@Param("userId") String userId);
}
