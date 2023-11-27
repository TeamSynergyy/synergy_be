package com.seoultech.synergybe.domain.follow.repository;

import com.seoultech.synergybe.domain.follow.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

    @Query(value = "SELECT following_id FROM follow WHERE follower_id = :userId AND status = 'FOLLOW'", nativeQuery = true)
    List<String> findFollowingIdsByFollowerId(@Param("userId") String userId);

    @Query(value = "SELECT follower_id FROM follow WHERE following_id = :userId AND status = 'FOLLOW'", nativeQuery = true)
    List<String> findFollowerIdsByFollowingId(@Param("userId") String userId);

    @Query(value = "SELECT * FROM follow WHERE follower_id = :userId AND following_id = :followingId FOR UPDATE", nativeQuery = true)
    Optional<Follow> findByFollowerIdAndFollowingId(@Param("userId") String userId, @Param("followingId") String followingId);
}
