package com.seoultech.synergybe.domain.postlike.repository;

import com.seoultech.synergybe.domain.postlike.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    @Query(value = "SELECT * FROM postlike WHERE user_id = :userId and post_id = :postId for update", nativeQuery = true)
    Optional<PostLike> findByUserUserIdAndPostId(@Param("userId") String userId, @Param("postId") Long postId);
}
