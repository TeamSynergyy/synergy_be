package com.seoultech.synergybe.domain.projectlike.repository;

import com.seoultech.synergybe.domain.projectlike.ProjectLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectLikeRepository extends JpaRepository<ProjectLike, Long> {

    @Query(value = "SELECT * FROM project_like WHERE user_id = :userId AND project_id = :projectId FOR UPDATE", nativeQuery = true)
    Optional<ProjectLike> findByUserUserIdAndProjectId(@Param("userId") String userId, @Param("projectId") Long projectId);

    @Query(value = "SELECT project_id FROM project_like WHERE user_id = :userId", nativeQuery = true)
    List<Long> findProjectIdsByUserId(@Param("userId") String userId);
}
